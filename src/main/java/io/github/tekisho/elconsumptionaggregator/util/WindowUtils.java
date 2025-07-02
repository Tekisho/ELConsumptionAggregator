package io.github.tekisho.elconsumptionaggregator.util;

import io.github.tekisho.elconsumptionaggregator.manager.StageManager;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public final class WindowUtils {
    private WindowUtils() {}

    public enum Side {
        LEFT,
        RIGHT
    }

    /**
     * Method allowing to place the required stage (which is logically considered as an additional window)
     * on the side with more free space, relative to the left and right edges of the parent window.
     * @param stageKey key from the stages hash map (StageManager)
     * @param defaultSide side that will be selected if the free space on both sides is equal
     */
    public static void setSecondaryStageCoordinates(StageManager.WindowType stageKey, WindowUtils.Side defaultSide) {
        StageManager manager = StageManager.getInstance();
        Stage mainStage = manager.getStage(StageManager.WindowType.MAIN);
        Stage targetStage = manager.getStage(stageKey);

        if (mainStage == null || targetStage == null) {
            throw new IllegalStateException("Stages not initialized");
        }

        double ownerX = mainStage.getX();
        double ownerWidth = mainStage.getWidth();
        double stageWidth = targetStage.getWidth();

        Screen screen = Screen.getScreensForRectangle(ownerX, mainStage.getY(), ownerWidth, mainStage.getHeight())
                .stream().findFirst().orElse(Screen.getPrimary());
        Rectangle2D bounds = screen.getVisualBounds();

        double availableSpaceLeft = ownerX - bounds.getMinX();
        double availableSpaceRight = bounds.getMaxX() - (ownerX + ownerWidth);

        double xPosition;
        final double EPSILON = 1;
        if (availableSpaceRight - availableSpaceLeft > EPSILON) {
            xPosition = ownerX + ownerWidth;
        } else if (availableSpaceLeft - availableSpaceRight > EPSILON) {
            xPosition = ownerX - stageWidth;
        } else {
            xPosition = (defaultSide == Side.LEFT) ? (ownerX - stageWidth) : (ownerX + ownerWidth);
        }

        double yPosition = mainStage.getY();

        manager.setCoordinates(stageKey, xPosition, yPosition);
    }
}
