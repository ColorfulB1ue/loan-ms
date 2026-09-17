package com.young.controller;

import com.young.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 电子合同控制器
 */
@Tag(name = "电子合同")
@RestController
@RequestMapping("/api/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Operation(summary = "生成贷款合同")
    @GetMapping("/generate/{loanId}")
    public void generateContract(@PathVariable Long loanId,
                                @RequestAttribute("userId") Long userId,
                                HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        String fileName = URLEncoder.encode("贷款合同_" + loanId + ".html", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "inline;filename=" + fileName);
        contractService.generateContract(loanId, userId, response.getOutputStream());
    }

    @Operation(summary = "下载贷款合同")
    @GetMapping("/download/{loanId}")
    public void downloadContract(@PathVariable Long loanId,
                                @RequestAttribute("userId") Long userId,
                                HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        String fileName = URLEncoder.encode("贷款合同_" + loanId + ".html", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        contractService.generateContract(loanId, userId, response.getOutputStream());
    }
}