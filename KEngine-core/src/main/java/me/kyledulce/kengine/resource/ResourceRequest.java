package me.kyledulce.kengine.resource;

public record ResourceRequest(Class<? extends GameAsset> resourceType, String resourceId) {
}