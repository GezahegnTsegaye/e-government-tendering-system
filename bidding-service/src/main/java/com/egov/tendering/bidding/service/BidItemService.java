package com.egov.tendering.bidding.service;

import com.egov.tendering.bidding.dal.dto.BidItemDTO;
import com.egov.tendering.bidding.dal.dto.BidItemRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BidItemService {
    @Transactional
    void saveBidItems(Long bidId, List<BidItemDTO> items);

    List<BidItemRequest> getBidItems(Long bidId);
}
