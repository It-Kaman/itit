package com.itit.controller;

import ch.qos.logback.classic.Logger;
import com.github.pagehelper.PageInfo;
import com.itit.entry.Article;
import com.itit.entry.User;
import com.itit.entry.admin.Admin;
import com.itit.exception.FileIOException;
import com.itit.query.ArticleQuery;
import com.itit.service.Ifac.ACCommentService;
import com.itit.service.Ifac.ArticleService;
import com.itit.util.COSUtil;
import com.itit.util.JsonModel;
import com.itit.util.StringUtil;
import com.itit.vo.ArticlePageVo;
import com.itit.vo.ArticleSearchVo;
import com.itit.vo.UploadArticleVo;
import com.qcloud.cos.COSClient;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/articles")
public class ArticleController {

    private Logger logger = (Logger) LoggerFactory.getLogger("test");
    @Value("${contentPath}")
    private String ContentPath;

    @Value("${BUCKET_NAME}")
    private String buckName;

    @Value("${COS_ARTICLE}")
    private String COS_Article;


    @Autowired
    private ArticleService articleService;

    @Autowired
    private COSClient cosClientUpload;

    @Autowired
    private ACCommentService acCommentService;

    //添加
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public JsonModel add(UploadArticleVo uploadArticleVo, HttpSession session){
        JsonModel js = new JsonModel();
        //判断标签是否为空
        if(uploadArticleVo.getColumn().size() ==0)
        {
            js.setMsg("请选择标签");
            return js;
        }
        FileOutputStream fileOutputStream = null;
        PrintWriter pw = null;
        String content = uploadArticleVo.getContent();
        //
        String datePath = StringUtil.dateFiles();
        String FilePath = ContentPath + datePath;
        File path = new File(FilePath);
        if (!path.exists()){
            path.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String fileName = simpleDateFormat.format(new Date());
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);

        File file = new File(path,"AC"+fileName+user.getId()+".txt");

        uploadArticleVo.setNum(fileName+user.getId());
        uploadArticleVo.setAuthor(user);
        uploadArticleVo.setStatus((byte)0);
        uploadArticleVo.setCreatedate(new Date());
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            pw = new PrintWriter(fileOutputStream,true);
            System.out.println(content);
            pw.println(content);
            //设置地址
            uploadArticleVo.setContent(file.getAbsolutePath());

            //提交
            articleService.add(uploadArticleVo);
            js.setSuccess(true);
//            //创建成功
//            if(COSUtil.selectCOS_Mkdir(cosClientUpload,COS_Article+datePath,buckName).size() == 0){
//                COSUtil.Create_Mkdir(cosClientUpload,buckName,COS_Article+datePath);
//            }
//            //上传文件
//            COSUtil.UpdateFile(cosClientUpload,buckName,COS_Article+datePath+"/"+file.getName(),file);
//            //获取URL
        } catch (IOException e) {
            e.printStackTrace();
            js.setMsg("服务器繁忙，请销后再试！");
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pw.close();
            return js;
        }
    }


    //显示对应的专栏页面
    @RequestMapping(value = "/page/AC{num}",method = RequestMethod.GET)
    public String page(@PathVariable(value = "num") String num,Model model,HttpSession session){
        //判断是否存在当前专栏且状态不为2
        BufferedReader reader = null;
        ArticlePageVo vo = null;
        ArticleQuery query = new ArticleQuery();
        query.setPagation(false);
        query.setNum(num);
        PageInfo<ArticlePageVo> pageInfo = articleService.findByQuery_second(query);
        StringBuffer content = new StringBuffer();
        User user = (User) session.getAttribute(StringUtil.USER_SESSION_KEY);
        Admin admin = (Admin) session.getAttribute(StringUtil.ADMIN_SESSION_KEY);
        if(pageInfo.getList().size()>0){
            vo = pageInfo.getList().get(0);
            if(vo.getStatus() == 2){
                //不通过的专栏
                model.addAttribute(StringUtil.MSG,"不存在当前专栏");
                return "common/error";
            }else if(vo.getAuthor().getId() != user.getId() && admin == null && vo.getStatus() == 0){
                return "redirect:/index";
            }
            try {
                if (!vo.getContent().startsWith("http")) {
                    File file = new File(vo.getContent());
                    if(file.exists()){
                        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                    }else {
                        return "redirect:/upload/article/record";
                    }
                } else {
                    reader = new BufferedReader(new InputStreamReader(new URL(vo.getContent()).openStream()));
                }
                String str = null;
                while ((str = reader.readLine())!= null){
                    content.append(str +"\n");
                }
            }catch (IOException e) {
                e.printStackTrace();
                logger.error("输入输出问题");
                throw new FileIOException("文件获取失败，请检查网络");
            }finally {
                try {
                    if(reader != null){
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            //找不到专栏，直接返回主页
            return "redirect:/index";
        }
        model.addAttribute("article",vo);
        model.addAttribute("content",content);
        model.addAttribute("date",StringUtil.DateFormate(null,vo.getCreatedate()));
        if(vo.getStatus() == 1){
            articleService.updateClickNum(vo.getId(),1);
        }
        //查询评论信息
        logger.info("成功进入该页面");
        return "article/articlePage";
    }


    //管理员查询
    @RequestMapping(value = "/admin",method = RequestMethod.GET)
    public String list(){
        return "admin/article";
    }

    @ResponseBody
    @RequestMapping(value = "/admin",method =RequestMethod.GET,headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel list(ArticleQuery articleQuery){
        JsonModel js = new JsonModel();
        articleQuery.setPagation(true);
        PageInfo<UploadArticleVo> datas = articleService.findByQueryLikename(articleQuery);
        js.setDatas(datas.getList());
        js.setCount(datas.getTotal());
        System.out.println(datas.getList());
        js.setSuccess(true);
        return js;
    }

    @ResponseBody
    @RequestMapping(value ="/admin/delete/{num}",method = RequestMethod.DELETE,headers = {"X-Requested-With=XMLHttpRequest"})
    public JsonModel delete(@PathVariable("num") String id)
    {
        JsonModel js = new JsonModel();
        String idStr[] = id.split(",");
        int ids[] = new int[idStr.length];
        for(int i = 0; i < idStr.length;i++){
            int Intid = Integer.parseInt(idStr[i]);
            ids[i] = Intid;
        }
        //越级删除
        articleService.deleteArticle(ids);
        js.setSuccess(true);
        return js;
    }

    @ResponseBody
    @PutMapping(value = "/admin/aduit/{ids}",headers ={"X-Requested-With=XMLHttpRequest"})
    public JsonModel aduit(@PathVariable("ids") String idsStr,Byte teacher_status,@RequestParam(value = "reason",required = false) String reason){
        System.out.println(reason);
        JsonModel jsonModel = new JsonModel();
        String[] strs = idsStr.split(",");
        int[] ids = new int[strs.length];
        for(int i = 0 ; i < strs.length;i++){
            ids[i] = Integer.parseInt(strs[i]);
        }
        if(articleService.aduitByIds(ids,teacher_status,reason) == 0)
        {
            jsonModel.setSuccess(false);
            return jsonModel;
        }
        jsonModel.setSuccess(true);
        return jsonModel;
    }
}
