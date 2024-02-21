package me.kyledulce.kengine.window.drawing;

import me.kyledulce.kengine.types.Color;
import me.kyledulce.kengine.types.Vector2;
import me.kyledulce.kengine.window.drawing.shader.ShaderProgram;
import me.kyledulce.kengine.window.drawing.texture.TextureAsset;

public class Sprite2D {
    private Vector2 location;
    private Vector2 rotation;
    private Vector2 scale;

    private TextureAsset texture;
    private ShaderProgram shaders;
    private Color shaderColor;
}
