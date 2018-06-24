package com.study.controller;

import com.study.service.UserService;
import com.study.util.ExcelImportUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

@Controller
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private UserService userService;
    //    导入用户数据
    @RequestMapping(value = "/uploadExcel")
    public String uploadExcel(@RequestParam("excelfile") MultipartFile file, RedirectAttributes model) {
        model.addFlashAttribute("flg", 1);
        //判断文件是否为空
        if (file == null) {
            model.addFlashAttribute("msg", "文件不能为空");
            return "redirect:/usersPage";
        }
        String fileName = file.getOriginalFilename();
        //验证文件名是否合格
        if (!ExcelImportUtils.validateExcel(fileName)) {
            model.addFlashAttribute("msg", "文件必须为excel");
            return "redirect:/usersPage";
        }
        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size = file.getSize();
        if (StringUtils.isEmpty(fileName) || size == 0) {
            model.addFlashAttribute("msg", "文件不能为空");
            return "redirect:/usersPage";
        }
        //批量导入

        String message = userService.batchImport(fileName, file);
        model.addFlashAttribute("msg", message);
        return "redirect:/usersPage";

    }
}
