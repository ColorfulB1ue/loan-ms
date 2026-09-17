package com.young.service;

import com.young.mapper.LoanApplicationMapper;
import com.young.mapper.LoanProductMapper;
import com.young.mapper.RepaymentPlanMapper;
import com.young.mapper.UserProfileMapper;
import com.young.pojo.LoanApplication;
import com.young.pojo.LoanProduct;
import com.young.pojo.RepaymentPlan;
import com.young.pojo.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 电子合同服务
 * 生成贷款合同PDF
 */
@Service
public class ContractService {

    private static final Logger log = LoggerFactory.getLogger(ContractService.class);

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private LoanProductMapper loanProductMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private RepaymentPlanMapper repaymentPlanMapper;

    /**
     * 生成贷款合同HTML内容
     */
    public String generateContractHtml(Long loanId, Long userId) {
        LoanApplication loan = loanApplicationMapper.selectById(loanId);
        if (loan == null || !loan.getUserId().equals(userId)) {
            throw new RuntimeException("贷款申请不存在或无权访问");
        }

        UserProfile profile = userProfileMapper.selectByUserId(userId);
        LoanProduct product = loan.getProductId() != null ? loanProductMapper.selectById(loan.getProductId()) : null;
        List<RepaymentPlan> plans = repaymentPlanMapper.selectByLoanId(loanId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String today = sdf.format(new Date());
        String applyDate = loan.getApplyTime() != null ? sdf.format(loan.getApplyTime()) : today;

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'>");
        html.append("<title>贷款合同</title>");
        html.append("<style>");
        html.append("body { font-family: 'SimSun', serif; line-height: 1.8; padding: 40px; max-width: 800px; margin: 0 auto; }");
        html.append("h1 { text-align: center; font-size: 24px; margin-bottom: 30px; }");
        html.append("h2 { font-size: 16px; margin: 20px 0 10px; }");
        html.append(".contract-info { margin: 15px 0; }");
        html.append(".contract-info label { font-weight: bold; }");
        html.append("table { width: 100%; border-collapse: collapse; margin: 15px 0; }");
        html.append("table th, table td { border: 1px solid #333; padding: 8px; text-align: left; }");
        html.append("table th { background-color: #f0f0f0; }");
        html.append(".signature { margin-top: 50px; display: flex; justify-content: space-between; }");
        html.append(".signature div { width: 45%; }");
        html.append(".footer { margin-top: 50px; text-align: center; color: #666; font-size: 12px; }");
        html.append("</style></head><body>");

        // 合同标题
        html.append("<h1>个人消费贷款合同</h1>");

        // 合同编号
        html.append("<div class='contract-info'>");
        html.append("<p><label>合同编号：</label>LOAN-").append(loanId).append("-").append(System.currentTimeMillis() % 10000).append("</p>");
        html.append("<p><label>签订日期：</label>").append(applyDate).append("</p>");
        html.append("</div>");

        // 甲方信息
        html.append("<h2>甲方（贷款人）信息</h2>");
        html.append("<div class='contract-info'>");
        html.append("<p><label>姓名：</label>").append(profile != null ? profile.getRealName() : "未知").append("</p>");
        html.append("<p><label>身份证号：</label>").append(profile != null ? maskIdCard(profile.getIdCard()) : "未知").append("</p>");
        html.append("<p><label>联系电话：</label>").append(profile != null ? profile.getPhone() : "未知").append("</p>");
        html.append("</div>");

        // 乙方信息
        html.append("<h2>乙方（贷款机构）信息</h2>");
        html.append("<div class='contract-info'>");
        html.append("<p><label>机构名称：</label>Loan-MS 贷款服务平台</p>");
        html.append("<p><label>联系方式：</label>400-XXX-XXXX</p>");
        html.append("</div>");

        // 贷款详情
        html.append("<h2>贷款详情</h2>");
        html.append("<div class='contract-info'>");
        html.append("<p><label>贷款产品：</label>").append(product != null ? product.getName() : "未知").append("</p>");
        html.append("<p><label>贷款金额：</label>人民币 ").append(formatAmount(loan.getAmount())).append(" 元</p>");
        html.append("<p><label>贷款期限：</label>").append(loan.getTermMonths()).append(" 个月</p>");
        html.append("<p><label>年利率：</label>").append(formatRate(loan.getAnnualRate())).append("%</p>");
        html.append("<p><label>贷款用途：</label>").append(loan.getPurpose() != null ? loan.getPurpose() : "个人消费").append("</p>");
        html.append("</div>");

        // 还款计划
        if (plans != null && !plans.isEmpty()) {
            html.append("<h2>还款计划</h2>");
            html.append("<table>");
            html.append("<tr><th>期数</th><th>应还日期</th><th>本金</th><th>利息</th><th>合计</th></tr>");
            for (RepaymentPlan plan : plans) {
                html.append("<tr>");
                html.append("<td>第").append(plan.getTermIndex()).append("期</td>");
                html.append("<td>").append(plan.getDueDate() != null ? sdf.format(plan.getDueDate()) : "-").append("</td>");
                html.append("<td>").append(formatAmount(plan.getPrincipal())).append("</td>");
                html.append("<td>").append(formatAmount(plan.getInterest())).append("</td>");
                html.append("<td>").append(formatAmount(plan.getTotalAmount())).append("</td>");
                html.append("</tr>");
            }
            html.append("</table>");
        }

        // 条款说明
        html.append("<h2>合同条款</h2>");
        html.append("<div class='contract-info'>");
        html.append("<p>1. 甲方应按照约定的还款计划按时足额还款，逾期将产生罚息。</p>");
        html.append("<p>2. 罚息按照逾期本金的0.05%/日计算，年化利率不超过24%。</p>");
        html.append("<p>3. 甲方提前还款需提前申请，提前还款不收取额外手续费。</p>");
        html.append("<p>4. 本合同自双方签署之日起生效，至贷款本息全部清偿之日终止。</p>");
        html.append("<p>5. 本合同一式两份，甲乙双方各执一份，具有同等法律效力。</p>");
        html.append("</div>");

        // 签章
        html.append("<div class='signature'>");
        html.append("<div><p><strong>甲方（签字）：</strong></p><p>________________</p><p>日期：________________</p></div>");
        html.append("<div><p><strong>乙方（盖章）：</strong></p><p>________________</p><p>日期：________________</p></div>");
        html.append("</div>");

        // 页脚
        html.append("<div class='footer'>");
        html.append("<p>本合同由 Loan-MS 贷款服务平台生成</p>");
        html.append("<p>生成时间：").append(today).append("</p>");
        html.append("</div>");

        html.append("</body></html>");

        return html.toString();
    }

    /**
     * 生成合同并写入输出流
     */
    public void generateContract(Long loanId, Long userId, OutputStream outputStream) throws IOException {
        String html = generateContractHtml(loanId, userId);
        outputStream.write(html.getBytes("UTF-8"));
    }

    private String formatAmount(BigDecimal amount) {
        if (amount == null) return "0.00";
        return amount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String formatRate(BigDecimal rate) {
        if (rate == null) return "0.00";
        // 将小数转换为百分比
        BigDecimal percent = rate.multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
        return percent.toPlainString();
    }

    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 8) return "未知";
        return idCard.substring(0, 4) + "****" + idCard.substring(idCard.length() - 4);
    }
}