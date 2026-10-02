# Recursos provisionales de Felox

El placeholder usa cajas sencillas y una textura RGBA de **64 × 64**, completamente verde (`#36C94A`). No representa el diseño final. Los recursos son originales de este prototipo y no se copiaron de Minecraft ni de los ejemplos de GeckoLib.

## Archivos

- `src/main/resources/assets/felox/geo/felox.geo.json`
- `src/main/resources/assets/felox/animations/felox.animation.json`
- `src/main/resources/assets/felox/textures/entity/felox.png`

En Blockbench, instala el plugin de GeckoLib, importa la geometría en un proyecto de modelo animado GeckoLib y carga la textura y el archivo de animaciones. Exporta de nuevo al formato compatible con GeckoLib 4 en las mismas rutas. No exportes como modelo Java de entidad vanilla.

## Huesos y animación

```text
root
├── body
│   ├── neck_01
│   │   └── head
│   ├── wing_left_root
│   ├── wing_right_root
│   └── tail_01
├── leg_left
└── leg_right
```

Las patas dependen provisionalmente de `root` para mantener los pies apoyados durante el idle. Antes de crear animaciones de vuelo, se revisará este detalle junto con los pivotes y la incorporación de segmentos articulados a las alas, cuello y cola.

`animation.felox.idle` es un loop de tres segundos. El controlador común `locomotion` solicita ese nombre exacto; cambiarlo en Blockbench requiere actualizar `FeloxAnimations`. Cada Felox tiene su propia caché de GeckoLib. No hay controladores de acciones, head tracking ni correcciones procedurales todavía.

Los UV pueden superponerse porque todos los píxeles tienen el mismo color. Antes de crear una textura artística, habrá que distribuirlos. La geometría no modifica la hitbox lógica, que se define en `ModEntities`.

## Validar una edición

1. Ejecuta `./gradlew build` para copiar los recursos.
2. Inicia `./gradlew runClient` y genera un Felox en terreno plano.
3. Observa al menos dos ciclos completos de idle, desde el frente y los lados.
4. Comprueba contacto de los pies, uniones del cuello y alas, textura verde y ausencia de errores en `run/logs/latest.log`.
5. Si usas `F3+T` para recargar, asegúrate de haber copiado antes los recursos actualizados con `./gradlew processResources`.

Al añadir animaciones futuras, asigna claramente qué huesos controla cada controlador. No presupongas que varios controladores de GeckoLib se mezclan de forma aditiva. Las correcciones procedurales deberán componerse sobre la pose animada sin acumular rotaciones entre fotogramas.
