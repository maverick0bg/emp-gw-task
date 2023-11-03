package com.emp.gw.task.controller;

import com.emp.gw.task.dto.MerchantDto;
import com.emp.gw.task.enums.MerchantUpdatableFields;
import com.emp.gw.task.service.MerchantService;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/merchant", produces = "application/json", consumes = "application/json")
@RequiredArgsConstructor
@Validated
public class MerchantController {

  private final MerchantService merchantService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MerchantDto createMerchant(@RequestBody @NotNull MerchantDto body) {
    return merchantService.createMerchant(body);
  }

  @GetMapping("/{merchantId}")
  @ResponseStatus(HttpStatus.OK)
  public MerchantDto getMerchant(@PathVariable("merchantId") @NotNull Long merchantId) {
    return merchantService.getMerchant(merchantId);
  }

  @PatchMapping("/{merchantId}")
  @ResponseStatus(HttpStatus.OK)
  public MerchantDto updateMerchant(
      @PathVariable("merchantId") @NotNull Long merchantId,
      @RequestBody @NotNull Map<MerchantUpdatableFields, Object> body) {
    return merchantService.updateMerchant(merchantId, body);
  }
}
