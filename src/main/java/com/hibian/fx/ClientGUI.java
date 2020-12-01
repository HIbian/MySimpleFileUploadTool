package com.hibian.fx;
/**
 * @author xin.chen
 * @date 2020/12/1 13:49
 */

import com.hibian.socket.fileupload.FileClient;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ClientGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        //1.创建Pane,用于承载控件
        AnchorPane anchorPane = new AnchorPane();

        //2.创建控件
        Label serverAddrLabel = new Label("服务器地址");
        serverAddrLabel.setLayoutX(37.0d);
        serverAddrLabel.setLayoutY(30.0d);
        Label filePathLabel = new Label("上传文件");
        filePathLabel.setLayoutX(37.0d);
        filePathLabel.setLayoutY(80.0d);
        TextField serverAddrText = new TextField();
        serverAddrText.setLayoutX(120.0d);
        serverAddrText.setLayoutY(26.0d);
        TextField filePathText = new TextField();
        filePathText.setLayoutX(120.0d);
        filePathText.setLayoutY(76.0d);
        Label stateLabel = new Label("上传状态");
        stateLabel.setLayoutX(130.0d);
        stateLabel.setLayoutY(120.0d);
        Button chooseFilePathButton = new Button("选择文件");
        chooseFilePathButton.setLayoutX(281.0d);
        chooseFilePathButton.setLayoutY(76.0d);
        //3.设置按钮点击事件
        chooseFilePathButton.setOnAction(event -> {
            FileChooser fileChooser=new FileChooser();
            File file = fileChooser.showOpenDialog(primaryStage);
            filePathText.setText(file==null?"":file.getPath());
        });


        Button uploadButton = new Button("上传文件");
        uploadButton.setLayoutX(281.0d);
        uploadButton.setLayoutY(113.0d);
        uploadButton.setOnAction(event -> {
            stateLabel.setText("状态:文件上传中...");
            try {
                String serverHost = getServerHost(serverAddrText.getText());
                Integer serverPort = getServetPort(serverAddrText.getText());
                FileClient.fileupload(filePathText.getText(), serverHost, serverPort);
                stateLabel.setText("状态:文件上传成功!");
            } catch (Exception e) {
                stateLabel.setText("状态:文件上传失败!");
            }
        });

        //4.添加控件到Pane
        anchorPane.getChildren().add(serverAddrLabel);
        anchorPane.getChildren().add(filePathLabel);
        anchorPane.getChildren().add(serverAddrText);
        anchorPane.getChildren().add(filePathText);
        anchorPane.getChildren().add(uploadButton);
        anchorPane.getChildren().add(stateLabel);
        anchorPane.getChildren().add(chooseFilePathButton);
        //5.将pane加入到Scen中
        Scene scene = new Scene(anchorPane, 360, 150);

        //6.设置stage的scen，显示stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("File Upload Client");
        primaryStage.show();
    }

    private Integer getServetPort(String text) {
        return Integer.valueOf(text.split(":")[1]);
    }

    private String getServerHost(String text) {
        return text.split(":")[0];
    }

}
