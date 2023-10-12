package me.kyledulce.kengine.window.drawing.shader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.kyledulce.kengine.resource.GameAsset;

@Getter
@RequiredArgsConstructor
public class ShaderAsset implements GameAsset {
    private final String shaderDefinition;

    @Setter(AccessLevel.PACKAGE)
    private int shaderId = 0;
    @Setter(AccessLevel.PACKAGE)
    private ShaderType shaderType;
}
