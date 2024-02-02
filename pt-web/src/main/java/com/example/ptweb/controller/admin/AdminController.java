package com.example.ptweb.controller.admin;

import com.example.ptbatch.utils.LocalDateTimeUtils;
import com.example.ptweb.dto.request.BulkPassRequest;
import com.example.ptweb.dto.response.AggregatesStatisticsResponse;
import com.example.ptweb.service.packze.PackageService;
import com.example.ptweb.service.pass.BulkPassService;
import com.example.ptweb.service.statistics.StatisticsService;
import com.example.ptweb.service.user.UserGroupMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author daecheol song
 * @since 1.0
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StatisticsService statisticsService;
    private final BulkPassService bulkPassService;
    private final PackageService packageService;
    private final UserGroupMappingService userGroupMappingService;

    @GetMapping
    public String home(@RequestParam(value = "to", required = false) String toString, Model model) {
        LocalDateTime to = LocalDateTimeUtils.parse(toString == null ?
                LocalDateTimeUtils.format(LocalDateTime.now(), LocalDateTimeUtils.YYYY_MM_DD_HH_MM) : toString);

        AggregatesStatisticsResponse aggregatesStatisticsResponse = statisticsService.getAggregatesStatisticsList(to);

        model.addAttribute("chartData", aggregatesStatisticsResponse);
        return "admin/index";
    }

    @GetMapping("/bulk-pass")
    public String bulkPass(Model model) {

        model.addAttribute("bulkPasses", bulkPassService.getAllBulkPasses());
        model.addAttribute("packages", packageService.getAllPackages());
        model.addAttribute("userGroupIds", userGroupMappingService.getAllUserGroupIds());
        model.addAttribute("request", new BulkPassRequest());

        return "admin/bulk-pass";
    }

    @PostMapping("/bulk-pass")
    public String addBulkPass(@ModelAttribute("request") BulkPassRequest request, Model model) {

        bulkPassService.addBulkPass(request);

        return "redirect:/admin/bulk-pass";
    }
}
