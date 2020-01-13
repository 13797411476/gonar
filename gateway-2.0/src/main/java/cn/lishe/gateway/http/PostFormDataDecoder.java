package cn.lishe.gateway.http;

import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PostFormDataDecoder {

    public Map<String, Map> recvData(List<InterfaceHttpData> datas) {
        /*
         * HttpDataType有三种类型
         * Attribute, FileUpload, InternalAttribute
         */
        Map<String, String> attributeMap = new HashMap<>();
        Map<String, FileUpload> fileMap = new HashMap<>();
        for (InterfaceHttpData data : datas) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                postDataAttribeParse(data, attributeMap);
            }
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                postDataFileUploadParse(data, fileMap);
            }
        }
        Map<String, Map> result = new HashMap<>();
        result.put("attributeMap", attributeMap);
        result.put("fileMap", fileMap);
        return result;
    }


    private boolean postDataAttribeParse(InterfaceHttpData data, Map<String, String> attributeMap) {
        Attribute attribute = (Attribute) data;
        String value;
        try {
            value = attribute.getValue();
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ":"
                    + attribute.getName() + " Error while reading value: " + e1.getMessage() + "\r\n");
            return false;
        }
        if (value.length() > 1024 * 10) {
            System.out.println("\r\nBODY Attribute: " + attribute.getHttpDataType().name() + ":"
                    + attribute.getName() + " data too long\r\n");
            return false;
        } else {
            attributeMap.put(attribute.getName(), value);
            return true;
        }
    }

    private boolean postDataFileUploadParse(InterfaceHttpData data, Map<String, FileUpload> fileMap) {
        final FileUpload fileUpload = (FileUpload) data;
        fileMap.put(fileUpload.getName(), fileUpload);
        return true;
    }


}
