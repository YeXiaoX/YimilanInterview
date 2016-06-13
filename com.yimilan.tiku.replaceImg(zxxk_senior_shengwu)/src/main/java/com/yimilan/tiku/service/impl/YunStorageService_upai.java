package com.yimilan.tiku.service.impl;

import com.yimilan.tiku.service.YunStorageService;
import com.yimilan.tiku.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xufeng on 15/4/7.
 */
@Service("yunStorageService")
public class YunStorageService_upai implements YunStorageService {

    private String upyun_operator_name = "xufeng";
    private String upyun_operator_pwd = "yMl@123qwe";

    /**
     * 默认的编码格式
     */
    private static final String UTF8 = "UTF-8";

    /**
     * 路径的分割符
     */
    private final String SEPARATOR = "/";

    private final String AUTHORIZATION = "Authorization";
    private final String DATE = "Date";
    private final String CONTENT_LENGTH = "Content-Length";
    private final String CONTENT_MD5 = "Content-MD5";
    private final String CONTENT_SECRET = "Content-Secret";
    private final String MKDIR = "mkdir";

    private final String X_UPYUN_WIDTH = "x-upyun-width";
    private final String X_UPYUN_HEIGHT = "x-upyun-height";
    private final String X_UPYUN_FRAMES = "x-upyun-frames";
    private final String X_UPYUN_FILE_TYPE = "x-upyun-file-type";
    private final String X_UPYUN_FILE_SIZE = "x-upyun-file-size";
    private final String X_UPYUN_FILE_DATE = "x-upyun-file-date";

    private final String METHOD_HEAD = "HEAD";
    private final String METHOD_GET = "GET";
    private final String METHOD_PUT = "PUT";
    private final String METHOD_DELETE = "DELETE";

    /**
     * 根据网络条件自动选择接入点:v0.api.upyun.com
     */
    public static final String ED_AUTO = "v0.api.upyun.com";
    /**
     * 电信接入点:v1.api.upyun.com
     */
    public static final String ED_TELECOM = "v1.api.upyun.com";
    /**
     * 联通网通接入点:v2.api.upyun.com
     */
    public static final String ED_CNC = "v2.api.upyun.com";
    /**
     * 移动铁通接入点:v3.api.upyun.com
     */
    public static final String ED_CTT = "v3.api.upyun.com";

    // 默认不开启debug模式
    public boolean debug = false;
    // 默认的超时时间：30秒
    private int timeout = 30 * 1000;
    // 默认为自动识别接入点
    private String apiDomain = ED_AUTO;
    // 待上传文件的 Content-MD5 值
    private String contentMD5 = null;
    // 待上传文件的"访问密钥"
    private String fileSecret = null;

    // 图片信息的参数
    protected String picWidth = null;
    protected String picHeight = null;
    protected String picFrames = null;
    protected String picType = null;

    // 文件信息的参数
    protected String fileType = null;
    protected String fileSize = null;
    protected String fileDate = null;


    public Boolean putObject(String bucketName, String fileName, byte[] datas) {


        if (HttpAction(METHOD_PUT, "/" + bucketName + "/" + fileName, datas, null, true, null) != null) {

            /*
            String[] split = fileName.split("\\.");

            if (ImageUtils.isImage(split[1])) {
                String thumbFileName = split[0] + "_thumb." + split[1];

                // 设置缩略图的参数
                Map<String, String> params = new HashMap<String, String>();

                // 设置缩略图类型，必须搭配缩略图参数值（KEY_VALUE）使用，否则无效
                params.put(PARAMS.KEY_X_GMKERL_TYPE.getValue(),
                        PARAMS.VALUE_FIX_BOTH.getValue());

                // 设置缩略图参数值，必须搭配缩略图类型（KEY_TYPE）使用，否则无效
                params.put(PARAMS.KEY_X_GMKERL_VALUE.getValue(), "250x250");

                // 设置缩略图的质量，默认 95
                params.put(PARAMS.KEY_X_GMKERL_QUALITY.getValue(), "95");

                // 设置缩略图的锐化，默认锐化（true）
                params.put(PARAMS.KEY_X_GMKERL_UNSHARP.getValue(), "true");

                // 若在 upyun 后台配置过缩略图版本号，则可以设置缩略图的版本名称
                // 注意：只有存在缩略图版本名称，才会按照配置参数制作缩略图，否则无效
                params.put(PARAMS.KEY_X_GMKERL_THUMBNAIL.getValue(), "small");


                return HttpAction(METHOD_PUT, "/" + bucketName + "/" + thumbFileName, datas, null, true, params) != null;

            }
            */
            return true;
        }

        return false;
    }

    public Boolean putObject(String bucketName, String fileName, File file) throws IOException {

        if (writeFile("/" + bucketName + "/" + fileName, file, true, null)) {

            String[] split = fileName.split("\\.");

            if (ImageUtils.isImage(split[1])) {

                String thumbFileName = split[0] + "_thumb." + split[1];

                // 设置缩略图的参数
                Map<String, String> params = new HashMap<String, String>();

                // 设置缩略图类型，必须搭配缩略图参数值（KEY_VALUE）使用，否则无效
                params.put(PARAMS.KEY_X_GMKERL_TYPE.getValue(),
                        PARAMS.VALUE_FIX_BOTH.getValue());

                // 设置缩略图参数值，必须搭配缩略图类型（KEY_TYPE）使用，否则无效
                params.put(PARAMS.KEY_X_GMKERL_VALUE.getValue(), "150x150");

                // 设置缩略图的质量，默认 95
                params.put(PARAMS.KEY_X_GMKERL_QUALITY.getValue(), "95");

                // 设置缩略图的锐化，默认锐化（true）
                params.put(PARAMS.KEY_X_GMKERL_UNSHARP.getValue(), "true");

                // 若在 upyun 后台配置过缩略图版本号，则可以设置缩略图的版本名称
                // 注意：只有存在缩略图版本名称，才会按照配置参数制作缩略图，否则无效
                params.put(PARAMS.KEY_X_GMKERL_THUMBNAIL.getValue(), "small");


                return writeFile("/" + bucketName + "/" + thumbFileName, file, true, params);

            }
            return true;
        }

        return false;
    }

    public Boolean deleteObject(String bucketName, String fileName) {

        return false;
    }


    /**
     * 连接处理逻辑
     *
     * @param method  请求方式 {GET, POST, PUT, DELETE}
     * @param uri     请求地址
     * @param datas   该请求所需发送数据（可为 null）
     * @param outFile 文件描述符（可为 null）
     * @param auto    自动创建父级目录(最多10级)
     * @param params  额外参数
     * @return 请求结果（字符串）或 null
     */
    private String HttpAction(String method, String uri, byte[] datas,
                              File outFile, boolean auto, Map<String, String> params) {

        String result = null;

        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;

        try {
            // 获取链接
            URL url = new URL("http://" + apiDomain + uri);
            conn = (HttpURLConnection) url.openConnection();

            // 设置必要参数
            conn.setConnectTimeout(timeout);
            conn.setRequestMethod(method);
            conn.setUseCaches(false);
            conn.setConnectTimeout(5 * 1000);
            if (!method.equals(METHOD_DELETE) && !method.equals(METHOD_HEAD) && !method.equals(METHOD_GET)) {
                conn.setDoOutput(true);
            }

            // 设置时间
            conn.setRequestProperty(DATE, getGMTDate());

            // 是否自动创建父级目录
            if (auto) {
                conn.setRequestProperty(MKDIR, "true");
            }

            long contentLength = 0;

            if (datas == null) {
                conn.setRequestProperty(CONTENT_LENGTH, "0");
            } else {
                contentLength = datas.length;
                conn.setRequestProperty(CONTENT_LENGTH,
                        String.valueOf(datas.length));

                this.contentMD5 = md5(datas);

                // 设置文件的 MD5 参数
                if (!isEmpty(this.contentMD5)) {
                    conn.setRequestProperty(CONTENT_MD5, this.contentMD5);
                    this.contentMD5 = null;
                }
                // 设置文件的访问密钥
                if (!isEmpty(this.fileSecret)) {
                    conn.setRequestProperty(CONTENT_SECRET, this.fileSecret);
                    this.fileSecret = null;
                }
            }

            // 设置签名
            conn.setRequestProperty(AUTHORIZATION,
                    sign(conn, uri, contentLength));

            // 是否是创建文件目录
            boolean isFolder = false;

            // 设置额外的参数，如图片缩略图等
            if (params != null && !params.isEmpty()) {

                isFolder = !isEmpty(params.get(PARAMS.KEY_MAKE_DIR.getValue()));

                for (Map.Entry<String, String> param : params.entrySet()) {
                    conn.setRequestProperty(param.getKey(), param.getValue());
                }
            }

            // 创建链接
            conn.connect();

            if (datas != null) {
                os = conn.getOutputStream();
                os.write(datas);
                os.flush();
            }

            if (isFolder) {
                os = conn.getOutputStream();
                os.flush();
            }

            if (outFile == null) {

                result = getText(conn, METHOD_HEAD.equals(method));

            } else {
                result = "";

                os = new FileOutputStream(outFile);
                byte[] data = new byte[4096];
                int temp = 0;

                is = conn.getInputStream();

                while ((temp = is.read(data)) != -1) {
                    os.write(data, 0, temp);
                }
            }
        } catch (IOException e) {
            if (debug)
                e.printStackTrace();

            // 操作失败
            return null;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {

            try {
                if (os != null) {
                    os.close();
                    os = null;
                }
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }

        return result;
    }


    /**
     * 上传文件
     *
     * @param filePath 文件路径（包含文件名）
     * @param file     待上传的文件
     * @param auto     是否自动创建父级目录(最多10级)
     * @param params   额外参数
     * @return true or false
     * @throws IOException
     */
    public boolean writeFile(String filePath, File file, boolean auto,
                             Map<String, String> params) throws IOException {
        this.contentMD5 = md5(file);

        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection conn = null;

        try {
            // 读取待上传的文件
            is = new FileInputStream(file);

            // 获取链接
            URL url = new URL("http://" + apiDomain + filePath);
            conn = (HttpURLConnection) url.openConnection();

            // 设置必要参数
            conn.setConnectTimeout(timeout);
            conn.setRequestMethod(METHOD_PUT);
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            // 设置时间
            conn.setRequestProperty(DATE, getGMTDate());
            // 设置签名
            conn.setRequestProperty(AUTHORIZATION,
                    sign(conn, filePath, is.available()));

            this.contentMD5 = md5(file);

            // 设置文件的 MD5 参数
            if (!isEmpty(this.contentMD5)) {
                conn.setRequestProperty(CONTENT_MD5, this.contentMD5);
                this.contentMD5 = null;
            }

            // 设置文件的访问密钥
            if (!isEmpty(this.fileSecret)) {
                conn.setRequestProperty(CONTENT_SECRET, this.fileSecret);
                this.fileSecret = null;
            }

            // 是否自动创建父级目录
            if (auto) {
                conn.setRequestProperty(MKDIR, "true");
            }

            // 设置额外的参数，如图片缩略图等
            if (params != null && !params.isEmpty()) {

                for (Map.Entry<String, String> param : params.entrySet()) {
                    conn.setRequestProperty(param.getKey(), param.getValue());
                }
            }

            // 创建链接
            conn.connect();

            os = conn.getOutputStream();
            byte[] data = new byte[4096];
            int temp = 0;

            // 上传文件内容
            while ((temp = is.read(data)) != -1) {
                os.write(data, 0, temp);
            }

            // 获取返回的信息
            getText(conn, false);

            // 上传成功
            return true;

        } catch (IOException e) {
            if (debug)
                e.printStackTrace();

            // 上传失败
            return false;

        } finally {

            if (os != null) {
                os.close();
                os = null;
            }
            if (is != null) {
                is.close();
                is = null;
            }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
    }

    /**
     * 上传文件
     *
     * @param filePath 文件路径（包含文件名）
     * @param datas    文件内容
     * @param auto     是否自动创建父级目录(最多10级)
     * @param params   额外参数
     * @return true or false
     */
    public boolean writeFile(String filePath, String datas, boolean auto,
                             Map<String, String> params) {

        boolean result = false;

        try {

            result = HttpAction(METHOD_PUT, filePath, datas.getBytes(UTF8), null, true, null) != null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 获得连接请求的返回数据
     *
     * @param conn
     * @return 字符串
     */
    private String getText(HttpURLConnection conn, boolean isHeadMethod)
            throws IOException {

        StringBuilder text = new StringBuilder();
        fileType = null;

        InputStream is = null;
        InputStreamReader sr = null;
        BufferedReader br = null;

        int code = conn.getResponseCode();

        try {
            is = code >= 400 ? conn.getErrorStream() : conn.getInputStream();

            if (!isHeadMethod) {
                sr = new InputStreamReader(is);
                br = new BufferedReader(sr);

                char[] chars = new char[4096];
                int length = 0;

                while ((length = br.read(chars)) != -1) {
                    text.append(chars, 0, length);
                }
            }
            if (200 == code && conn.getHeaderField(X_UPYUN_WIDTH) != null) {
                picWidth = conn.getHeaderField(X_UPYUN_WIDTH);
                picHeight = conn.getHeaderField(X_UPYUN_HEIGHT);
                picFrames = conn.getHeaderField(X_UPYUN_FRAMES);
                picType = conn.getHeaderField(X_UPYUN_FILE_TYPE);
            } else {
                picWidth = picHeight = picFrames = picType = null;
            }

            if (200 == code && conn.getHeaderField(X_UPYUN_FILE_TYPE) != null) {
                fileType = conn.getHeaderField(X_UPYUN_FILE_TYPE);
                fileSize = conn.getHeaderField(X_UPYUN_FILE_SIZE);
                fileDate = conn.getHeaderField(X_UPYUN_FILE_DATE);
            } else {
                fileType = fileSize = fileDate = null;
            }
        } finally {
            if (br != null) {
                br.close();
                br = null;
            }
            if (sr != null) {
                sr.close();
                sr = null;
            }
            if (is != null) {
                is.close();
                is = null;
            }
        }

        if (isHeadMethod) {
            if (code >= 400)
                return null;
            return "";
        }

        if (code >= 400)
            throw new IOException(text.toString());

        return text.toString();
    }


    /**
     * 对字符串进行 MD5 加密
     *
     * @param str 待加密字符串
     * @return 加密后字符串
     */
    public static String md5(String str) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes(UTF8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        byte[] encodedValue = md5.digest();
        int j = encodedValue.length;
        char finalValue[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte encoded = encodedValue[i];
            finalValue[k++] = hexDigits[encoded >> 4 & 0xf];
            finalValue[k++] = hexDigits[encoded & 0xf];
        }

        return new String(finalValue);
    }

    /**
     * 对文件进行 MD5 加密
     *
     * @param file 待加密的文件
     * @return 文件加密后的 MD5 值
     * @throws IOException
     */
    public static String md5(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            int n = 0;
            byte[] buffer = new byte[1024];
            do {
                n = is.read(buffer);
                if (n > 0) {
                    md5.update(buffer, 0, n);
                }
            } while (n != -1);
            is.skip(0);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            is.close();
        }

        byte[] encodedValue = md5.digest();

        int j = encodedValue.length;
        char finalValue[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte encoded = encodedValue[i];
            finalValue[k++] = hexDigits[encoded >> 4 & 0xf];
            finalValue[k++] = hexDigits[encoded & 0xf];
        }

        return new String(finalValue);
    }

    /**
     * 对文件进行 MD5 加密
     *
     * @param
     * @return 文件加密后的 MD5 值
     * @throws IOException
     */
    public static String md5(byte[] fileBytes) throws IOException, NoSuchAlgorithmException {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(fileBytes, 0, fileBytes.length);

        byte[] encodedValue = md5.digest();

        int j = encodedValue.length;
        char finalValue[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte encoded = encodedValue[i];
            finalValue[k++] = hexDigits[encoded >> 4 & 0xf];
            finalValue[k++] = hexDigits[encoded & 0xf];
        }

        return new String(finalValue);
    }

    /**
     * 获取 GMT 格式时间戳
     *
     * @return GMT 格式时间戳
     */
    private String getGMTDate() {
        SimpleDateFormat formater = new SimpleDateFormat(
                "EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        formater.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formater.format(new Date());
    }

    /**
     * 计算签名
     *
     * @param conn   连接
     * @param uri    请求地址
     * @param length 请求所发Body数据长度
     * @return 签名字符串
     */
    private String sign(HttpURLConnection conn, String uri, long length) {
        String sign = conn.getRequestMethod() + "&" + uri + "&"
                + conn.getRequestProperty(DATE) + "&" + length + "&" + md5(upyun_operator_pwd);
        return "UpYun " + upyun_operator_name + ":" + md5(sign);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return 是否为空
     */
    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 其他额外参数的键值和参数值
     */
    public enum PARAMS {

        /**
         * 缩略图类型
         * <p/>
         * 使用场景：上传图片时若无需保存原图，而只需某种特定大小的缩略图，比如说用户头像。
         * <p/>
         * 说明：该参数必须搭配 KEY_X_GMKERL_VALUE 使用，否则无效。另外，使用该参数后将不保存原图，切忌。
         * <p/>
         * 可选参数：<br>
         * 1)VALUE_FIX_MAX("fix_max")："限定最长边，短边自适应"<br>
         * 2)VALUE_FIX_MIN("fix_min")："限定最短边，长边自适应"<br>
         * 3)VALUE_FIX_WIDTH_OR_HEIGHT("fix_width_or_height")："限定宽度和高度"<br>
         * 4)VALUE_FIX_WIDTH("fix_width")："限定宽度，高度自适应"<br>
         * 5)VALUE_FIX_HEIGHT("fix_height")："限定高度，宽度自适应"<br>
         * 6)VALUE_FIX_BOTH("fix_both")："固定宽度和高度"<br>
         * 7)VALUE_FIX_SCALE("fix_scale")："等比例缩放"<br>
         * 8)VALUE_SQUARE("square")："方块图，固定高固定宽"<br>
         *
         * @see ：http://wiki.upyun.com/index.php?title=缩略图方式差别举例
         */
        KEY_X_GMKERL_TYPE("x-gmkerl-type"),

        /**
         * 缩略图参数值
         * <p/>
         * 说明：该参数必须搭配 KEY_X_GMKERL_TYPE 使用，否则无效。具体的值需要根据 KEY_X_GMKERL_TYPE 而定。
         */
        KEY_X_GMKERL_VALUE("x-gmkerl-value"),

        /**
         * 缩略图质量：图片压缩质量，默认 95
         * <p/>
         * 使用场景：用户上传高保真图片，但自身业务又无需太高质量的图片时，可以设置该参数减少文件保存的大小，从而减少空间的使用量。
         * <p/>
         * 说明：使用该参数后将不保存原图，切忌。
         */
        KEY_X_GMKERL_QUALITY("x-gmkerl-quality"),

        /**
         * 图片锐化：默认锐化（true）
         * <p/>
         * 使用场景：图片处理后质量太差，可以使用该参数模糊边缘，提高图片的清晰度或者焦距程度，使图片特定区域的色彩更加鲜明。
         * <p/>
         * 说明：锐化不是万能的，很容易使图片不真实；另外，也无法通过锐化达到原图的效果。
         */
        KEY_X_GMKERL_UNSHARP("x-gmkerl-unsharp"),

        /**
         * 缩略图版本
         * <p/>
         * 使用场景：快速处理原图，生成自定义的缩略图。
         * <p/>
         * 说明：使用该参数前需要创建好缩略图版本号；另外，使用该参数后将不保存原图，切忌。
         * <p/>
         * http://wiki.upyun.com/index.php?title=如何创建自定义缩略图
         */
        KEY_X_GMKERL_THUMBNAIL("x-gmkerl-thumbnail"),

        /**
         * 图片旋转
         * <p/>
         * 使用场景：待上传的图片若是倾斜的，使用该参数可以直接进行强制的或自动的扶正。
         * <p/>
         * 说明：只接受"auto"，"90"，"180"，"270"四种参数，其中"auto"参数根据图片 EXIF
         * 中的信息进行自动扶正，若图片没有 EXIF 信息，则该参数无效。另外，使用该参数后将不保存原图，切忌。
         * <p/>
         * http://wiki.upyun.com/index.php?title=图片旋转
         */
        KEY_X_GMKERL_ROTATE("x-gmkerl-rotate"),

        /**
         * 图片裁剪
         * <p/>
         * 使用场景：只需要保存待上传图片的某一个部分，比如用户上传头像图片进行裁剪。
         * <p/>
         * 说明：参数格式为x,y,width,height，且需要满足 x >= 0 && y >=0 && width > 0 && height
         * > 0
         * <p/>
         * http://wiki.upyun.com/index.php?title=图片裁剪
         */
        KEY_X_GMKERL_CROP("x-gmkerl-crop"),

        /**
         * 是否保留exif信息
         * <p/>
         * 使用场景：对于原图包含EXIF信息，在上传图片时又进行了“破坏性处理”（比如裁剪、缩略、自定义版本等），
         * upyun默认会删除原图的EXIF信息。 此时搭配该参数可以保留原图的EXIF信息。比如旅游应用从缩略图中获取具体的地理信息。
         * <p/>
         * 说明：仅搭配"破坏性处理"的参数使用时有效，其他处理均无效；另外key对应的值仅设置为"true"时有效；
         */
        KEY_X_GMKERL_EXIF_SWITCH("x-gmkerl-exif-switch"),

        /**
         * 创建目录
         * <p/>
         * 说明：SDK内部使用
         */
        KEY_MAKE_DIR("folder"),

        /**
         * 缩略图类型之 "限定最长边，短边自适应"，参数为像素值，如: 150
         */
        VALUE_FIX_MAX("fix_max"),
        /**
         * 缩略图类型之 "限定最短边，长边自适应"，参数为像素值，如: 150
         */
        VALUE_FIX_MIN("fix_min"),
        /**
         * 缩略图类型之 "限定宽度和高度"，参数为像素值，如: 150x130
         */
        VALUE_FIX_WIDTH_OR_HEIGHT("fix_width_or_height"),
        /**
         * 缩略图类型之 "限定宽度，高度自适应"，参数为像素值，如: 150
         */
        VALUE_FIX_WIDTH("fix_width"),
        /**
         * 缩略图类型之 "限定高度，宽度自适应"，参数为像素值，如: 150
         */
        VALUE_FIX_HEIGHT("fix_height"),
        /**
         * 缩略图类型之 "方块图，固定高固定宽"，参数为像素值，如: 150
         */
        VALUE_SQUARE("square"),
        /**
         * 缩略图类型之 "固定宽度和高度"，参数为像素值，如: 150x130
         */
        VALUE_FIX_BOTH("fix_both"),
        /**
         * 缩略图类型之 "等比例缩放"，参数为比例值（1-99），如: 50
         */
        VALUE_FIX_SCALE("fix_scale"),

        /**
         * 图片旋转之 "自动扶正"
         */
        VALUE_ROTATE_AUTO("auto"),
        /**
         * 图片旋转之 "旋转90度"
         */
        VALUE_ROTATE_90("90"),
        /**
         * 图片旋转之 "旋转180度"
         */
        VALUE_ROTATE_180("180"),
        /**
         * 图片旋转之 "旋转270度"
         */
        VALUE_ROTATE_270("270");

        private final String value;

        private PARAMS(String val) {
            value = val;
        }

        public String getValue() {
            return value;
        }
    }

}



