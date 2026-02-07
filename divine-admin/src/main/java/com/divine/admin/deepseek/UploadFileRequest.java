package com.divine.admin.deepseek;

import lombok.Data;
import java.io.File;
import java.io.InputStream;

/**
 * 文件上传请求实体类
 * 支持多种方式提供文件内容
 */
@Data
public class UploadFileRequest {

    /**
     * 文件对象
     * 如果使用文件对象方式上传
     */
    private File file;

    /**
     * 文件输入流
     * 如果使用流方式上传
     */
    private InputStream inputStream;

    /**
     * 文件字节数组
     * 如果使用字节数组方式上传
     */
    private byte[] fileBytes;

    /**
     * Base64编码的文件内容
     * 如果使用Base64方式上传
     */
    private String base64Content;

    /**
     * 文件名（包含扩展名）
     * 例如: "document.pdf", "image.png"
     */
    private String fileName;

    /**
     * 文件MIME类型
     * 例如: "application/pdf", "image/png"
     * 如果为null，将根据文件名自动推断
     */
    private String mimeType;

    /**
     * 文件用途描述
     * 例如: "analysis", "translation", "summary"
     */
    private String purpose;

    /**
     * 是否将文件内容转换为文本
     * 如果为true，将提取文件中的文本内容
     */
    private boolean extractText = true;

    /**
     * 文件最大大小限制（字节）
     * 默认20MB
     */
    private long maxFileSize = 20 * 1024 * 1024;

    /**
     * 支持的图片格式
     */
    private static final String[] SUPPORTED_IMAGE_FORMATS = {
        "jpg", "jpeg", "png", "gif", "bmp"
    };

    /**
     * 支持的文档格式
     */
    private static final String[] SUPPORTED_DOCUMENT_FORMATS = {
        "pdf", "txt", "doc", "docx", "xls", "xlsx", "ppt", "pptx"
    };

    /**
     * 验证文件格式是否支持
     */
    public boolean isFileFormatSupported() {
        if (fileName == null) return false;

        String extension = getFileExtension().toLowerCase();

        // 检查图片格式
        for (String format : SUPPORTED_IMAGE_FORMATS) {
            if (format.equals(extension)) {
                return true;
            }
        }

        // 检查文档格式
        for (String format : SUPPORTED_DOCUMENT_FORMATS) {
            if (format.equals(extension)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取文件扩展名
     */
    public String getFileExtension() {
        if (fileName == null) return "";
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot + 1) : "";
    }

    /**
     * 自动推断MIME类型
     */
    public String inferMimeType() {
        if (mimeType != null) {
            return mimeType;
        }

        String extension = getFileExtension().toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            default:
                return "application/octet-stream";
        }
    }
}
