package me.kyledulce.kengine.window.drawing.texture;

import lombok.*;
import me.kyledulce.kengine.resource.GameAsset;

import java.nio.ByteBuffer;

@Getter
@RequiredArgsConstructor
public class TextureAsset implements GameAsset {
    private final ByteBuffer imgBuffer;
    private final int height;
    private final int width;

    @Setter(AccessLevel.PACKAGE)
    private int id = 0;
    @Setter(AccessLevel.PACKAGE)
    private boolean unloaded = false;
}
