package com.camille.seckill.controller;

import com.camille.seckill.pojo.User;
import com.camille.seckill.service.IGoodsService;
import com.camille.seckill.service.IUserService;
import com.camille.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    /*@Autowired
    private IUserService userService;*/

    @Autowired
    private IGoodsService goodsService;


    @RequestMapping("/toList")
    public String toList(Model model,User user){
        /*if(!StringUtils.hasText(ticket)){
            return "login";
        }
        User user = userService.getUserByTicket(ticket, request, response);
        if(user == null){
            return "login";
        }*/
        model.addAttribute("user",user);

        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goodsList";
    }

    @RequestMapping("/toDetail/{goodsId}")
    public String toDetail(Model model, User user, @PathVariable(value = "goodsId") long GoodsId){
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsById(GoodsId);
        model.addAttribute("goods", goodsVo);

        return "goodsDetail";
    }

}
