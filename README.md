# Felox

Mod de Minecraft Java sobre una criatura aérea tipo ave y dragón. **Etapa 1 / 0.1.0:** entidad pasiva, modelo provisional verde y animación `idle` de GeckoLib. Todavía permanece en el suelo; no hay vuelo, combate, sonidos personalizados ni generación natural.

## Herramientas

| Componente | Versión |
| --- | --- |
| Minecraft Java | 1.20.1 |
| Forge | 47.4.0 |
| Java JDK | 17 |
| Gradle Wrapper | 8.8 |
| ForgeGradle | 6.0.36 |
| GeckoLib para Forge 1.20.1 | 4.7.2 |
| Mappings | Mojmap (`official`, 1.20.1) |

Las versiones se fijan en `gradle.properties`, `build.gradle` y `gradle/wrapper/gradle-wrapper.properties`. No se necesita Gradle instalado globalmente. El wrapper comprueba el SHA-256 de su distribución. GeckoLib se descarga como dependencia; no se incluye dentro del JAR de Felox.

## Compilar e importar en IntelliJ IDEA

1. Instala un **JDK 17** y configura `JAVA_HOME` para apuntar a él; comprueba `java -version`.
2. Abre esta carpeta como proyecto Gradle en IntelliJ. Selecciona **Gradle Wrapper** y **Gradle JVM: Java 17**.
3. Ejecuta desde la raíz:

   ```bash
   ./gradlew build
   ./gradlew genIntellijRuns
   ```

En Windows utiliza `gradlew.bat` en lugar de `./gradlew`. La primera ejecución necesita Internet para descargar Minecraft, Forge, Gradle y GeckoLib. El JAR reobfuscado queda en `build/libs/felox-0.1.0.jar`.

## Probar el cliente

```bash
./gradlew runClient
```

También puedes usar la configuración `runClient` generada para IntelliJ. Crea un mundo creativo con trucos habilitados, busca **Felox** en la pestaña de huevos generadores o ejecuta:

```mcfunction
/summon felox:felox ~ ~ ~
/give @s felox:felox_spawn_egg
```

Comprueba en terreno seco y plano:

- El modelo es verde y tiene cuerpo, cabeza, alas, patas y cola visibles.
- La animación `idle` repite cada tres segundos, moviendo cuerpo, cabeza, alas y cola.
- La criatura no busca jugadores, no ataca y no contraataca al recibir daño. Sigue siendo vulnerable.
- `F3+B` muestra una sola hitbox de 1,4 × 1,8 bloques; las alas y la cola pueden sobresalir.
- Varias criaturas animan sin compartir el estado de animación.
- Al salir y volver a entrar al mundo, Felox sigue presente.
- `F3+T` recarga sus recursos sin errores de GeckoLib ni texturas ausentes.

Se mantiene quieto intencionalmente para revisar el modelo. Aún se aplican gravedad, empujones y daño ambiental normales; no es un prototipo de navegación ni de supervivencia en agua.

## Servidor y pruebas automáticas

```bash
./gradlew runGameTestServer
```

Ejecuta **tres GameTests** en un servidor dedicado: registro/atributos y guardado/carga, uso real del huevo, y pasividad tras daño del jugador. Termina al completar las pruebas y devuelve un estado de error si fallan. Un arranque sin pruebas ejecutadas no valida la etapa. Los tests y su estructura están en `src/gameTest`, fuera del JAR distribuido.

`./gradlew build` compila también los GameTests, pero **no los ejecuta**. El workflow de GitHub ejecuta ambos comandos por separado.

Para una sesión multijugador de desarrollo:

```bash
./gradlew runServer
```

El servidor usa `run-server/`. En el primer arranque revisa el EULA de Minecraft y, si lo aceptas, establece `eula=true` en `run-server/eula.txt`; luego repite el comando. No se incluye una aceptación automática en el repositorio. Los mundos del cliente (`run/`), servidor y GameTests (`run-gametest/`) están separados e ignorados por Git.

Para instalar en un cliente/servidor normal, usa Forge 1.20.1 y coloca el JAR de Felox y GeckoLib Forge 1.20.1 4.7.2 en `mods/` en ambos lados. La validación visual se hace con un cliente; los GameTests por sí solos no comprueban el renderizado.

## Organización

- `Felox.java`: punto de entrada y conexión de registros.
- `registry/`: entidad, atributos, huevo y pestaña creativa.
- `entity/FeloxEntity.java`: infraestructura del mob pasivo y caché de animación por instancia.
- `entity/animation/FeloxAnimations.java`: controlador `locomotion` de GeckoLib, seguro en servidor.
- `client/`: registro de renderer limitado a `Dist.CLIENT`, modelo y renderer.
- `assets/felox/`: geometría, animación, textura, traducciones y modelo del huevo.
- `src/gameTest/`: verificaciones funcionales sobre Forge.

Los valores iniciales de vida, velocidades, alcance y daño futuro están centralizados en `FeloxEntity.createAttributes()` y son atributos normales de Minecraft. No hay aún un archivo de balance ni un sistema de combate. No se han creado estados, sensores o controladores vacíos para etapas futuras.

## Modelo provisional y siguiente etapa

Consulta [la guía de recursos](docs/modelo.md) para editar el placeholder en Blockbench. Es una prueba técnica propia, no el diseño artístico definitivo.

La siguiente etapa será vuelo hacia un waypoint con aceleración, giros limitados e inercia. La IA propondrá el destino y un controlador separado aplicará el movimiento en el servidor. Felox seguirá siendo pasivo.
