package io.github.tekisho.elconsumptionaggregator.util;
import io.github.tekisho.elconsumptionaggregator.ELConsumptionAggregator;
import javafx.scene.image.Image;

import java.net.URL;

public final class ResourceLoader {
    private ResourceLoader() {}

    public static Image loadImage(String path) {
        return new Image(getResource(path).toExternalForm());
    }

    public static URL loadFXML(String path) {
        return getResource(path);
    }

    public static String loadStyleSheet(String path) {
        return getResource(path).toExternalForm();
    }

    private static URL getResource(String path) {
        URL resource = ELConsumptionAggregator.class.getResource(path);
        if (resource == null) {
            throw new ResourceNotFoundException(
                    String.format("Resource not found: %s (make sure it exists in src/main/resources)", path)
            );
        }
        return resource;
    }

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
