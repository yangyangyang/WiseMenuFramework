package io.github.gefangshuai.server.controller;

import io.github.gefangshuai.rtat.model.Restaurant;
import io.github.gefangshuai.rtat.service.RestaurantService;
import io.github.gefangshuai.constant.ResultCode;
import io.github.gefangshuai.server.core.context.AppConfigContext;
import io.github.gefangshuai.utils.StoreUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by gefangshuai on 2015/11/10.
 */
@Controller
@RequestMapping("/account")
public class AccountFormController {
    @Resource
    private RestaurantService restaurantService;
    @Resource
    private AppConfigContext appConfigContext;

    @ModelAttribute("restaurant")
    public Restaurant bindRestaurant(@PathVariable long id) {
        Restaurant restaurant;
        if (id > 0) {
            restaurant = restaurantService.findOne(id);
        } else {
            restaurant = new Restaurant();
        }
        return restaurant;
    }

    /**
     * 补充信息
     */
    @RequestMapping("additional/{id}")
    public String additionalRestaurant() {
        return "account/restaurantInfoModal";
    }

    /**
     * 保存店铺信息
     *
     * @param restaurant
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "restaurant/{id}/save", method = RequestMethod.POST)
    public ResultCode saveRestaurant(@ModelAttribute("restaurant") Restaurant restaurant, double xText, double yText, double widthText, double heightText, MultipartFile file) {

        if (file != null) {
            try {
                String relativePath = StoreUtils.storeCutFile(appConfigContext.getStorePath(), StoreUtils.getExtension(file.getOriginalFilename()), file.getInputStream(), (int) xText, (int) yText, (int) widthText, (int) heightText);
                restaurant.setImagePath(relativePath);
            } catch (IOException e) {
                e.printStackTrace();
                return ResultCode.Faild;
            }
        }
        restaurantService.update(restaurant);
        restaurantService.updateSession(restaurant);
        return ResultCode.OK;
    }
}
