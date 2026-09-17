package com.young.controller;

import com.young.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 数据导出控制器
 */
@Tag(name = "数据导出")
@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @Operation(summary = "导出贷款记录")
    @GetMapping("/loans")
    public void exportLoans(@RequestAttribute("userId") Long userId, 
                           HttpServletResponse response) throws IOException {
        setResponseHeaders(response, "贷款记录");
        exportService.exportLoanApplications(userId, response.getOutputStream());
    }

    @Operation(summary = "导出还款计划")
    @GetMapping("/plans")
    public void exportPlans(@RequestAttribute("userId") Long userId,
                           HttpServletResponse response) throws IOException {
        setResponseHeaders(response, "还款计划");
        exportService.exportRepaymentPlans(userId, response.getOutputStream());
    }

    @Operation(summary = "导出还款记录")
    @GetMapping("/records")
    public void exportRecords(@RequestAttribute("userId") Long userId,
                             HttpServletResponse response) throws IOException {
        setResponseHeaders(response, "还款记录");
        exportService.exportRepaymentRecords(userId, response.getOutputStream());
    }

    private void setResponseHeaders(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String encodedFileName = URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
    }
}