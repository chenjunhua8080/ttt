package com.cjh.ttt.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.ttt.dto.MessageDetailDto;
import com.cjh.ttt.dto.MessageDto;
import com.cjh.ttt.request.MessageRequest;
import com.cjh.ttt.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息表(Message)表控制层
 *
 * @author cjh
 * @since 2020-03-25 18:16:49
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/msg")
public class MessageController {

    private MessageService messageService;

    /**
     * 我的会话列表
     *
     * @param page 分页对象
     */
    @GetMapping("/list")
    public R list(Page<MessageDto> page) {
        return R.ok(messageService.getMessageList(page));
    }

    /**
     * 会话明细
     *
     * @param messageId 主键
     * @param page      分页对象
     */
    @GetMapping("/detail/list")
    public R info(Page<MessageDetailDto> page, @RequestParam("messageId") Integer messageId) {
        return R.ok(messageService.getDetailList(page, messageId));
    }

    /**
     * 删除会话
     */
    @PostMapping("delete")
    public R delete(@RequestBody MessageRequest messageRequest) {
        messageService.delete(messageRequest.getMessageId());
        return R.ok("删除成功");
    }

    /**
     * 删除消息明细
     */
    @PostMapping("/detail/delete")
    public R deleteMessageDetail(@RequestBody MessageRequest messageRequest) {
        messageService.deleteMessageDetail(messageRequest.getMessageDetailId());
        return R.ok("删除成功");
    }


    /**
     * 撤回消息
     */
    @PostMapping("/detail/withdraw")
    public R withdraw(@RequestBody MessageRequest messageRequest) {
        messageService.withdraw(messageRequest.getMessageDetailId());
        return R.ok("撤回成功");
    }

    /**
     * 发送消息
     */
    @PostMapping("/send")
    public R send(@RequestBody MessageRequest messageRequest) {
        messageService.send(messageRequest.getMessageId(),messageRequest.getContent());
        return R.ok("已送达");
    }

}