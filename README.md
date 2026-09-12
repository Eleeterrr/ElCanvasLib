# ElCanvasLib

2D shape renderer using SDFs, built on LWJGL/OpenGL. Draws rects, circles, rounded rects, capsules, rings, triangles with solid or gradient fills, plus glow, outline and drop shadow effects.

## Requirements

You need an active OpenGL context already running, this lib doesn't make a window or context for you. Everything needs to run on the GL thread.

## Setup

```java
SdfCanvas canvas = SdfCanvas.create();
canvas.init();
```

## Render loop

```java
canvas.begin(viewportWidth, viewportHeight);

// draw calls here

canvas.end();
```

Wrap every frame's draw calls with `begin` and `end`.

## Drawing shapes

```java
canvas.drawRect(x, y, width, height, Color.RED);
canvas.drawCircle(centerX, centerY, radius, Color.WHITE);
canvas.drawRoundedRect(x, y, width, height, 12f, Color.BLUE);
canvas.drawCapsule(x, y, width, height, Color.GREEN);
canvas.drawRing(centerX, centerY, radius, 6f, Color.YELLOW);
canvas.drawTriangle(x, y, width, height, Color.CYAN);
```

Rounded rect can take separate corner radii too (top left, top right, bottom right, bottom left):

```java
canvas.drawRoundedRect(x, y, width, height, 20f, 0f, 20f, 0f, fill);
```

Want effects on a shape, just toss them in as a list or varargs:

```java
canvas.drawCircle(cx, cy, radius, fill, new GlowEffect(Color.CYAN, 20f));
```

### Builder

Grab this when you need rotation, a pivot point, or more than one effect on the same shape.

```java
canvas.drawRoundedRect(16f)
    .at(x, y)
    .size(width, height)
    .fill(Color.fromHex("#3498db"))
    .rotationDegrees(15f)
    .pivot(0.5f, 0.5f)
    .with(new DropShadowEffect(Color.BLACK.withAlpha(0.4f), 8f))
    .with(OutlineEffect.outer(Color.WHITE, 2f))
    .draw();
```

Nothing shows up until you call `.draw()`.

Entry points: `drawRect()`, `drawCircle()`, `drawRoundedRect(radius)`, `drawRoundedRect(tl, tr, br, bl)`, `drawCapsule()`, `drawRing(thickness)`, `drawTriangle()`.

Builder methods:

- `at(x, y)`
- `size(width, height)`
- `bounds(x, y, width, height)`
- `fill(Fill)` or `color(Fill)`
- `rotation(radians)` or `rotationDegrees(degrees)`
- `pivot(normalizedX, normalizedY)`, default is 0.5, 0.5
- `with(effect)`
- `withAll(list)`
- `draw()`

## Fills

### Solid color

```java
Color.RED
Color.rgb(0.2f, 0.4f, 0.9f)
Color.rgba(0.2f, 0.4f, 0.9f, 0.5f)
Color.fromHex("#3498db")
Color.fromHex("#3498db80")
```

Constants you get for free: `WHITE`, `BLACK`, `RED`, `GREEN`, `BLUE`, `YELLOW`, `CYAN`, `MAGENTA`, `TRANSPARENT`.

`color.withAlpha(0.5f)` hands you back a new color, doesn't touch the original.

### Linear gradient

```java
LinearGradient.horizontal(Color.RED, Color.BLUE);
LinearGradient.vertical(Color.RED, Color.BLUE);
LinearGradient.diagonal(Color.RED, Color.BLUE);
```

With stops (2 to 5 of them, coordinates are 0 to 1 normalized):

```java
new LinearGradient(0f, 0f, 1f, 1f,
    GradientStop.of(0f, Color.RED),
    GradientStop.of(0.5f, Color.YELLOW),
    GradientStop.of(1f, Color.GREEN));
```

### Radial gradient

```java
RadialGradient.centered(Color.WHITE, Color.BLACK);
RadialGradient.of(cx, cy, radius, centerColor, edgeColor);
```

Same stop rules as linear.

## Effects

They stack in the order you add them.

### Glow

```java
new GlowEffect(Color.CYAN, radius);
new GlowEffect(Color.CYAN, radius, intensity);
```

### Outline

```java
OutlineEffect.outer(Color.WHITE, thickness);
OutlineEffect.centered(Color.WHITE, thickness);
OutlineEffect.inner(Color.WHITE, thickness);
```

### Drop shadow

```java
new DropShadowEffect(Color.BLACK.withAlpha(0.5f), blurAmount);
new DropShadowEffect(color, offsetX, offsetY, blur);
new DropShadowEffect(color, offsetX, offsetY, blur, spread);
```

## Cleanup

```java
canvas.dispose();
```

Or just use try with resources since `SdfCanvas` is `AutoCloseable`:

```java
try (SdfCanvas canvas = SdfCanvas.create()) {
    canvas.init();
    // render loop
}
```

## Full example

```java
SdfCanvas canvas = SdfCanvas.create();
canvas.init();

canvas.begin(windowWidth, windowHeight);

canvas.drawRoundedRect(20f)
    .bounds(100, 100, 300, 150)
    .fill(Color.fromHex("#4ecdc4"))
    .with(OutlineEffect.outer(Color.WHITE, 2f))
    .draw();

canvas.drawCircle(400, 400, 50, Color.YELLOW, new GlowEffect(Color.YELLOW, 25f));

canvas.end();

canvas.dispose();
```

## Why

Made this for myself, needed something for shape rendering that didn't make me deal with old stuff like tessellation.

## License

MIT license.