package io.github.tekisho.elconsumptionaggregator.util;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;

public final class TooltipUtils {
    public static void bindTooltipIfClipped(Label label) {
        ChangeListener<Object> changeListener = (observableValue, oldValue, newValue) -> updateTooltip(label);

        label.textProperty().addListener(changeListener);
        label.widthProperty().addListener(changeListener);
        label.fontProperty().addListener(changeListener);

        Platform.runLater(() -> updateTooltip(label));
    }

    private static void updateTooltip(Label label) {
        if (isTextClipped(label)) {
            Tooltip tooltip = label.getTooltip();
            if (tooltip == null) {
                tooltip = new Tooltip(label.getText());
                label.setTooltip(tooltip);
            } else {
                tooltip.setText(label.getText());
            }
        } else {
            label.setTooltip(null);
        }

    }

    private static boolean isTextClipped(Label label) {
        if (label.getText() == null || label.getText().isEmpty()) {
            return false;
        }

        Text text = new Text(label.getText());
        text.setFont(label.getFont());

        double textWidth = text.getLayoutBounds().getWidth();
        double labelWidth = label.getWidth() - label.getPadding().getLeft() - label.getPadding().getRight();

        return textWidth > labelWidth;
    }
}
