/*
 * Copyright (C) 2024-2026 Provismet
 *
 * See https://github.com/Provismet/LilyLib/blob/1.21/LICENSE for the full license.
 */

package com.provismet.lilylib.datagen.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public abstract class LilyParticleTextureProvider implements DataProvider {
    protected final FabricPackOutput output;
    private final PackOutput.PathProvider pathResolver;
    private final CompletableFuture<HolderLookup.Provider> registryLookup;

    protected LilyParticleTextureProvider (FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        this.output = output;
        this.pathResolver = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "particles");
        this.registryLookup = registryLookup;
    }

    protected abstract void generate (HolderLookup.Provider registryLookup, ParticleWriter writer);

    @Override
    public CompletableFuture<?> run (CachedOutput writer) {
        return this.registryLookup.thenCompose(lookup -> {
            List<ParticleEntry> particleEntries = new ArrayList<>();
            ParticleWriter particleWriter = new ParticleWriter(particleEntries);

            this.generate(lookup, particleWriter);

            final List<CompletableFuture<?>> futures = new ArrayList<>();
            for (ParticleEntry entry : particleEntries) {
                JsonObject json = new JsonObject();
                JsonArray textures = new JsonArray();
                for (Identifier texture : entry.textures) {
                    textures.add(texture.toString());
                }
                json.add("textures", textures);
                futures.add(DataProvider.saveStable(writer, json, this.getPath(entry.identifier())));
            }

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }

    private Path getPath (Identifier soundId) {
        return this.pathResolver.json(soundId);
    }

    @Override
    public String getName () {
        return "ParticleTexture";
    }

    protected static final class ParticleWriter {
        private final List<ParticleEntry> entries;

        private ParticleWriter (List<ParticleEntry> entries) {
            this.entries = entries;
        }

        public void add (Identifier particle, Identifier... textures) {
            Objects.requireNonNull(particle);
            Objects.requireNonNull(textures);
            this.entries.add(new ParticleEntry(particle, textures));
        }

        public void add (Identifier particle, List<Identifier> textures) {
            Objects.requireNonNull(textures);
            this.add(particle, textures.toArray(Identifier[]::new));
        }

        public void add (ParticleType<?> particle, Identifier... textures) {
            this.add(BuiltInRegistries.PARTICLE_TYPE.getKey(particle), textures);
        }

        public void add (ParticleType<?> particle, List<Identifier> textures) {
            this.add(BuiltInRegistries.PARTICLE_TYPE.getKey(particle), textures);
        }
    }

    private record ParticleEntry (Identifier identifier, Identifier... textures) {

    }
}
