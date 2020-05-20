package com.itit.controller;

import com.github.pagehelper.PageInfo;
import com.itit.entry.SearchTag;
import com.itit.entry.Tag;
import com.itit.query.*;
import com.itit.service.Ifac.*;
import com.itit.util.JsonModel;
import com.itit.util.SearchUtil;
import com.itit.util.StringUtil;
import com.itit.vo.ArticleSearchVo;
import com.itit.vo.UserSearchVo;
import com.itit.vo.VideoSearchVo;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private UserService userService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private SearchTagService searchTagService;
    @Autowired
    private TagService tagService;


    @RequestMapping(value = "",method = RequestMethod.GET)
    public String page(HttpServletRequest request, String name,
                       @RequestParam(value = "status",required = false)Byte status,
                       @RequestParam(value = "tag",required = false)String tags){
        System.out.println(name);
        System.out.println(status);
        System.out.println(tags);
        System.out.println(StringUtil.isEmpty(tags));
        if(!StringUtil.isEmpty(tags)){
            status = 4;
            //查询当前的标签是那一个
            TagQuery query = new TagQuery();
            query.setName(tags);
            Tag tag = tagService.findByQuery(query);
            if(tag == null){
                return "redirect:/search";
            }else{
                request.setAttribute("tag",tag.getId());
            }
        }else if(StringUtil.isEmpty(name)){
            return "search/Search";
        }
        if(status == null){
            status = 0;
        }
        request.setAttribute("name",name);
        request.setAttribute("status",status);
        return "search/SearchPage";
    }

    @ResponseBody
    @RequestMapping(value = "",method = RequestMethod.GET,headers = {"X-Requested-With=XMLHttpRequest"})
    public Map<String,Object> page(SearchQuery searchQuery,@RequestParam(value = "articleColumn",required = false) String column){
        System.out.println(searchQuery);
        PageInfo pageInfo=null;
        Map<String,Object> map = new HashMap<>();
        map.put("success",false);
        if(StringUtil.isEmpty(searchQuery.getName()) && searchQuery.getColumn() == null && column == null){
            map.put("msg","没有内容");
            return map;
        }
        if(searchQuery.getStatus() == 1){
            //用户
            UserQuery userQuery = new UserQuery();
            userQuery = (UserQuery) SearchUtil.setObjectQuery(userQuery,searchQuery);
            pageInfo = userService.queryByUserQuery(userQuery);
            for(UserSearchVo uvo : (List<UserSearchVo>)pageInfo.getList()){
                uvo.setFansNum(StringUtil.NumFormate(Integer.parseInt(uvo.getFansNum())));
            }
        }else if(searchQuery.getStatus() == 2){
            VideoQuery videoQuery = new VideoQuery();
            videoQuery = (VideoQuery) SearchUtil.setObjectQuery(videoQuery,searchQuery);
            pageInfo = videoService.queryByVideoQuery(videoQuery);
            for (VideoSearchVo vvo : (List<VideoSearchVo>)pageInfo.getList()){
                int num = 0;
                if(!StringUtil.isEmpty(vvo.getHotNum())){
                    num = Integer.parseInt(vvo.getHotNum());
                    vvo.setHotNum(StringUtil.NumFormate(num));
                }
                else {
                    vvo.setHotNum(""+num);
                }
                if(StringUtil.isEmpty(vvo.getNum())){
                    vvo.setNum("");
                }
            }
        }else if(searchQuery.getStatus() == 3){
            ArticleQuery articleQuery = new ArticleQuery();
            articleQuery = (ArticleQuery) SearchUtil.setObjectQuery(articleQuery,searchQuery);
            pageInfo = articleService.searchArticle(articleQuery);
        }else if(searchQuery.getStatus() == 4){
            //0 视频 1 专栏
            if(searchQuery.getTagStatus() == 1){
                String[] columnStr = column.split(",");
                Integer[] columns = null;
                columns = (Integer[]) ConvertUtils.convert(columnStr, Integer.class);

                if(columns[0] == 0){
                    return map;
                }
                ArticleQuery articleQuery = new ArticleQuery();
                articleQuery.setName(searchQuery.getName());
                articleQuery.setPagation(true);
                articleQuery.setPageNum(searchQuery.getPageNum());
                articleQuery.setPageSize(searchQuery.getPageSize());
                articleQuery.setStatus((byte)1);
                articleQuery.setColumn(columns);
                pageInfo = articleService.searchArticle(articleQuery);
            }else {
                VideoQuery videoQuery = new VideoQuery();
                videoQuery.setPagation(true);
                videoQuery.setName(searchQuery.getName());
                videoQuery.setPageNum(searchQuery.getPageNum());
                videoQuery.setPageSize(searchQuery.getPageSize());
                videoQuery.setStatus((byte)1);
                videoQuery.setColumn(searchQuery.getColumn());
                pageInfo = videoService.queryByVideoQuery(videoQuery);
                for (VideoSearchVo vvo : (List<VideoSearchVo>)pageInfo.getList()){
                    int num = 0;
                    if(!StringUtil.isEmpty(vvo.getHotNum())){
                        num = Integer.parseInt(vvo.getHotNum());
                        vvo.setHotNum(StringUtil.NumFormate(num));
                    }
                    else {
                        vvo.setHotNum(""+num);
                    }
                    if(StringUtil.isEmpty(vvo.getNum())){
                        vvo.setNum("");
                    }
                }
            }

        }
        map.put("datas",pageInfo.getList());
        map.put("count",pageInfo.getTotal());
        map.put("MaxPageNum",pageInfo.getPages());
        map.put("success",true);
        return map;
    }
    @RequestMapping(value = "/toolbar",method = RequestMethod.GET)
    public String userPage(SearchQuery searchQuery)
    {
        System.out.println(searchQuery);
        Byte status = searchQuery.getStatus();
        if(status == 1){
            return "/search/UserSearchPage";
        }else if(status == 2){
            return "/search/VideoSearchPage";
        }else if(status == 3){
            return "/search/ArticleSearchPage";
        }else if(status == 0){
            return "/search/AllSeachPage";
        }else if(status == 4){
            return "/search/TagSearchPage";
        }
//        return "redirect:/search";
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/hot",headers = "X-Requested-With=XMLHttpRequest",method = RequestMethod.GET)
    public Map<String,Object> searchlist(SearchTagQuery query){
        Map<String,Object> map = new HashMap<>();
        map.put("success",false);
        if(query.getPageSize() == null){
            query.setPageSize(15);
        }
        query.setPageNum(0);
        query.setPagation(true);
        PageInfo<SearchTag> pageInfo = searchTagService.queryByQuery(query);
        Random rand = new Random();
        int showHot = rand.nextInt(query.getPageSize());
        SearchTag showtag = pageInfo.getList().get(showHot);
        map.put("success",true);
        map.put("datas",pageInfo.getList());
        map.put("showtag",showtag);
        map.put("count",pageInfo.getPageSize());
        System.out.println(map);
        return map;
    }
}