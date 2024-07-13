<div align="center">

# LilyLib
[![](https://img.shields.io/jitpack/version/com.github.Provismet/LilyLib?style=flat-square&logo=jitpack&color=F6F6F6)](https://jitpack.io/#Provismet/LilyLib)

</div>

I've started needing to reuse some of my modding code, so now I need a library I guess.

LilyLib is a general content and utility library for Minecraft. It aims to provide general support such as:

### Minecraft/Fabric specific utility:
- Datagen provider for enchantments.
- Datagen provider for damage types.
- Datagen provider for damage type tags.
- Container classes for Enchantments and DamageTypes.
- A more malleable renderer for item-based entities.
  - Interface for such entities to implement.
- Particle subclass for floor particles (such as AoE rings).
- Entity relation checker.

### General utility
- JSON string builder.
- Additional math methods.

## Registries
LilyLib is intended to be used in client-side mods, server-side mods, and mods that exist on both.  
For this reason, LilyLib does not call any registry methods by itself. Mods that require LilyLib features to be registered should call the `register` methods themselves.

LilyLib's register methods will internally check if they have been called before. It is safe for multiple mods to call the methods.

Registry types include:
- Fabric Resource Conditions

## License
The library is licensed as GNU-LGPL, so feel free to use it and package it within a mod. Though I have no idea why anyone would want to.

## Dependency
Add to your build.gradle
```gradle
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```

```gradle
dependencies {
    modImplementation "com.github.provismet:lilylib:${project.lilylib_version}"
    include "com.github.provismet:lilylib:${project.lilylib_version}"
}
```

Where `${project.lilylib_version}` can either be replaced with a release tag, or you can define a `lilylib_version` parameter in your gradle.properties file.

Check the [Jitpack.io](https://jitpack.io/#Provismet/LilyLib) page for working builds.
