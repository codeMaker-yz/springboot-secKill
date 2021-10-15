package com.camille.seckill.controller;

import com.camille.seckill.pojo.User;
import com.camille.seckill.service.IGoodsService;
import com.camille.seckill.service.IUserService;
import com.camille.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    /*@Autowired
    private IUserService userService;*/

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 优化前QPS：Windows 1000线程连接，循环10次，吞吐量1913，重复3次，吞吐量900
     *
     * @param model
     * @param user
     * @return
     */

    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model,User user,HttpServletRequest request,HttpServletResponse response){
        String html = (String) redisTemplate.opsForValue().get("goodsList");
        if(StringUtils.hasText(html)){
           return html;
        }


        model.addAttribute("user",user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());

        WebContext webContext = new WebContext(request,response, request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", webContext);
        if(StringUtils.hasText(html)){
            redisTemplate.opsForValue().set("goodsList", html,60, TimeUnit.SECONDS);
        }

        return html;
    }



    @RequestMapping(value = "/toDetail/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail(Model model, User user, @PathVariable(value = "goodsId") long GoodsId,
                           HttpServletRequest request,HttpServletResponse response){
        String html = (String) redisTemplate.opsForValue().get("goodDetails:" + GoodsId);
        if(StringUtils.hasText(html)){
           return html;
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsById(GoodsId);
        LocalDateTime startDate = goodsVo.getStartDate();
        LocalDateTime endDate = goodsVo.getEndDate();
        LocalDateTime nowDate = LocalDateTime.now();

        int secKillStatus;
        long remainSeconds;
        if(nowDate.isBefore(startDate)){
            Duration duration = Duration.between(nowDate, startDate);
            remainSeconds = duration.toMillis() / 1000;
            secKillStatus = 0;
        } else if(nowDate.isAfter(endDate)){
            secKillStatus = 2;
            remainSeconds = -1;
        } else {
            secKillStatus = 1;
            remainSeconds = 0;
        }
        System.out.println(remainSeconds);
        System.out.println(secKillStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("secKillStatus", secKillStatus);
        model.addAttribute("goods", goodsVo);

        WebContext webContext = new WebContext(request,response, request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
        if(StringUtils.hasText(html)){
            redisTemplate.opsForValue().set("goodDetails:" + GoodsId, html,60, TimeUnit.SECONDS);
        }

        return html;
    }

}
