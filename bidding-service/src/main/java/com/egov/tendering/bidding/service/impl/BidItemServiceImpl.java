package com.egov.tendering.bidding.service.impl;


import com.egov.tendering.bidding.dal.dto.BidItemDTO;
import com.egov.tendering.bidding.dal.dto.BidItemRequest;
import com.egov.tendering.bidding.dal.mapper.BidItemMapper;
import com.egov.tendering.bidding.dal.model.BidItem;
import com.egov.tendering.bidding.dal.repository.BidItemRepository;
import com.egov.tendering.bidding.service.BidItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidItemServiceImpl implements BidItemService {

    private final BidItemRepository bidItemRepository;
    private final BidItemMapper bidItemMapper;

    /**
     * Save a list of bid items.
     */
    @Transactional
    @Override
    public void saveBidItems(Long bidId, List<BidItemDTO> items) {
        List<BidItem> bidItems = items.stream()
                .map(item -> {
                    BidItem bidItem = bidItemMapper.toEntity(item);
                    bidItem.setId(bidId);
                    return bidItem;
                })
                .toList();
        bidItemRepository.saveAll(bidItems);
    }

    /**
     * Get bid items for a specific bid.
     */
    @Override
    public List<BidItemRequest> getBidItems(Long bidId) {
        return bidItemRepository.findByBidId(bidId)
                .stream()
                .map(bidItemMapper::toRequest)
                .toList();
    }
}