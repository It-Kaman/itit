package com.itit.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.exception.MultiObjectDeleteException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.qcloud.cos.transfer.Upload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class COSUtil {
    private final static int MAX_KEYS = 1000;
//    查询文件夹对象

    /**
     *
     * @param cosClient 客户端
     * @param path    查找文件夹路径
     * @param bucketName  存储桶名字
     * @return
     */
    public static List<String> selectCOS_Mkdir(COSClient cosClient,String path,String bucketName){
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucketName);
        listObjectsRequest.setPrefix(path);
        listObjectsRequest.setDelimiter("/");
        listObjectsRequest.setMaxKeys(MAX_KEYS);
        ObjectListing objectListing = null;
        List<String> commonPrefixs = null;
        do{
            try{
                objectListing = cosClient.listObjects(listObjectsRequest);
            }catch (CosServiceException e){
                e.printStackTrace();
                return null;
            }catch (CosClientException e){
                e.printStackTrace();
                return null;
            }
            commonPrefixs = objectListing.getCommonPrefixes();
        }while (objectListing.isTruncated());
        return commonPrefixs;
    }


    public static void Create_Mkdir(COSClient cosClient,String bucketName,String path){
        path = path +"/";
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(0);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,path,input,objectMetadata);
        cosClient.putObject(putObjectRequest);
    }

    public static void UpdateFile(COSClient cosClient,String bucketName, String keyPath, File file) throws InterruptedException {
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartCopyPartSize(ConstrainUtil.MULTIPARTUPLOADTHRESHOLD);
        transferManagerConfiguration.setMinimumUploadPartSize(ConstrainUtil.MINIMUMUPLOADPARTSIZE);
        TransferManager transferManager = new TransferManager(cosClient,Executors.newFixedThreadPool(10));
        transferManager.setConfiguration(transferManagerConfiguration);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,keyPath,file);
        Upload upload = transferManager.upload(putObjectRequest);
        UploadResult uploadResult = upload.waitForUploadResult();
        transferManager.shutdownNow();
    }

    /*删除文件*/
    public static void deleteObject(COSClient cosClient,String bucketName,List<String> deleteFiles){
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
// 设置要删除的key列表, 最多一次删除1000个
        ArrayList<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<DeleteObjectsRequest.KeyVersion>();
// 传入要删除的文件名
        for(String filePath : deleteFiles){
            keyList.add(new DeleteObjectsRequest.KeyVersion(filePath));
        }
        deleteObjectsRequest.setKeys(keyList);

        //批量删除
        try {
            DeleteObjectsResult deleteObjectsResult = cosClient.deleteObjects(deleteObjectsRequest);
            List<DeleteObjectsResult.DeletedObject> deleteObjectResultArray = deleteObjectsResult.getDeletedObjects();
        } catch (MultiObjectDeleteException mde) { // 如果部分删除成功部分失败, 返回MultiObjectDeleteException
            List<DeleteObjectsResult.DeletedObject> deleteObjects = mde.getDeletedObjects();
            List<MultiObjectDeleteException.DeleteError> deleteErrors = mde.getErrors();
        } catch (CosServiceException e) { // 如果是其他错误，例如参数错误， 身份验证不过等会抛出 CosServiceException
            e.printStackTrace();
            throw e;
        } catch (CosClientException e) { // 如果是客户端错误，例如连接不上COS
            e.printStackTrace();
            throw e;
        }
    }
}
