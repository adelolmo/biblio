package org.ado.biblio.desktop.android;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import org.ado.biblio.desktop.AppConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class description here.
 *
 * @author andoni
 * @since 22.11.2014
 */
public class AndroidPresenter implements Initializable {

    private final Logger LOGGER = LoggerFactory.getLogger(AndroidPresenter.class);

    @FXML
    private ImageView imageViewQrCode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createQrCode();
    }

    private void createQrCode() {
        try {
            LOGGER.info("App ID: {}", AppConfiguration.getAppId());
            LOGGER.info("Server IP: {}", AppConfiguration.getApplicationProperty("server.host"));
            BitMatrix matrix = new MultiFormatWriter().encode(getCode(AppConfiguration.getAppId(), AppConfiguration.getApplicationProperty("server.host")), BarcodeFormat.QR_CODE, 300, 300);
            final BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);

            imageViewQrCode.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private String getCode(String appId, String serverHost) {
        return appId.concat("+").concat(serverHost);
    }
}
