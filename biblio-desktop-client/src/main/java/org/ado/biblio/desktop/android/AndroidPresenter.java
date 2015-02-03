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

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Andoni del Olmo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
            LOGGER.info("Server IP: {}", AppConfiguration.getConfigurationProperty("server.url"));
            BitMatrix matrix = new MultiFormatWriter().encode(getCode(AppConfiguration.getAppId(), AppConfiguration.getConfigurationProperty("server.url")), BarcodeFormat.QR_CODE, 300, 300);
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
