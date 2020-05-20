package com.itit.util;

import com.itit.query.*;

public class SearchUtil {
    public static Object setObjectQuery(Object object, SearchQuery searchQuery){
        UserQuery userQuery = null;
        VideoQuery videoQuery =null;
        ArticleQuery articleQuery = new ArticleQuery();
        if(object instanceof UserQuery){
            userQuery = new UserQuery();
            userQuery.setPagation(true);
            userQuery.setAnothername(searchQuery.getName());
            userQuery.setPageNum(searchQuery.getPageNum());
            userQuery.setPageSize(searchQuery.getPageSize());
            System.out.println(searchQuery.isTime());
            if (searchQuery.isFans()){
                userQuery.setFans(true);
            }
            return userQuery;
        }else if(object instanceof VideoQuery){
            videoQuery = new VideoQuery();
            videoQuery.setPagation(true);
            videoQuery.setName(searchQuery.getName());
            videoQuery.setPageNum(searchQuery.getPageNum());
            videoQuery.setPageSize(searchQuery.getPageSize());
            videoQuery.setStatus((byte)1);
            //默认热度查询
            if(searchQuery.isTime()){
                videoQuery.setTime(true);
            }
            return videoQuery;
        }else if(object instanceof ArticleQuery){
            articleQuery = new ArticleQuery();
            if(searchQuery.isTime() == true){
                articleQuery.setTime(true);
            };
            articleQuery.setPagation(true);
            articleQuery.setPageNum(searchQuery.getPageNum());
            articleQuery.setPageSize(searchQuery.getPageSize());
            articleQuery.setName(searchQuery.getName());
            articleQuery.setStatus((byte)1);
            return articleQuery;
        }
        return null;
    }
}
