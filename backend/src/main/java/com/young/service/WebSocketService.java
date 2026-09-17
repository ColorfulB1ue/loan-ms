package com.young.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket通知服务
 */
@Service
public class WebSocketService {

    private static final Logger log = LoggerFactory.getLogger(WebSocketService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 发送通知给指定用户
     */
    public void sendToUser(Long userId, String type, String title, String content) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", type);
            message.put("title", title);
            message.put("content", content);
            message.put("timestamp", System.currentTimeMillis());

            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/queue/notifications",
                    message
            );
            log.debug("发送WebSocket通知给用户 {}: {}", userId, title);
        } catch (Exception e) {
            log.error("发送WebSocket通知失败: {}", e.getMessage());
        }
    }

    /**
     * 广播通知给所有用户
     */
    public void broadcast(String type, String title, String content) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", type);
            message.put("title", title);
            message.put("content", content);
            message.put("timestamp", System.currentTimeMillis());

            messagingTemplate.convertAndSend("/topic/broadcast", message);
            log.debug("广播WebSocket通知: {}", title);
        } catch (Exception e) {
            log.error("广播WebSocket通知失败: {}", e.getMessage());
        }
    }

    /**
     * 发送贷款审批结果通知
     */
    public void sendLoanApprovalNotification(Long userId, boolean approved, String loanId) {
        String type = approved ? "LOAN_APPROVED" : "LOAN_REJECTED";
        String title = approved ? "贷款申请已通过" : "贷款申请被拒绝";
        String content = approved
                ? "您的贷款申请（编号：" + loanId + "）已审批通过，款项将尽快发放。"
                : "您的贷款申请（编号：" + loanId + "）未通过审批，请查看详情。";
        sendToUser(userId, type, title, content);
    }

    /**
     * 发送还款提醒通知
     */
    public void sendRepaymentReminder(Long userId, String planId, String dueDate) {
        sendToUser(userId, "REPAYMENT_REMINDER", "还款提醒",
                "您有一笔还款将于" + dueDate + "到期，请及时还款。");
    }

    /**
     * 发送催收通知
     */
    public void sendCollectionNotice(Long userId, String planId) {
        sendToUser(userId, "COLLECTION_NOTICE", "催收通知",
                "您有一笔逾期账单，请尽快处理。");
    }
}