package com.egov.tendering.bidding.controller;

import com.egov.tendering.bidding.dal.dto.BidDTO;
import com.egov.tendering.bidding.dal.dto.PageDTO;
import com.egov.tendering.bidding.dal.model.BidStatus;
import com.egov.tendering.bidding.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.egov.tendering.bidding.dal.dto.BidSubmissionRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
@Slf4j
public class BidController {

  private final BidService bidService;

  @PostMapping
  public ResponseEntity<BidDTO> createBid(
          @Valid @RequestBody BidSubmissionRequest request,
          @RequestHeader("X-User-ID") Long tendererId) {

    log.info("Creating bid for tender ID: {} by tenderer ID: {}", request.getTenderId(), tendererId);
    BidDTO createdBid = bidService.createBid(request, tendererId);
    return new ResponseEntity<>(createdBid, HttpStatus.CREATED);
  }

  @GetMapping("/{bidId}")
  public ResponseEntity<BidDTO> getBidById(@PathVariable Long bidId) {
    log.info("Getting bid by ID: {}", bidId);
    BidDTO bid = bidService.getBidById(bidId);
    return ResponseEntity.ok(bid);
  }

  @PostMapping("/{bidId}/submit")
  public ResponseEntity<BidDTO> submitBid(@PathVariable Long bidId) {
    log.info("Submitting bid with ID: {}", bidId);
    BidDTO submittedBid = bidService.submitBid(bidId);
    return ResponseEntity.ok(submittedBid);
  }

  @PatchMapping("/{bidId}/status")
  public ResponseEntity<BidDTO> updateBidStatus(
          @PathVariable Long bidId,
          @RequestParam BidStatus status) {

    log.info("Updating bid status to {} for bid ID: {}", status, bidId);
    BidDTO updatedBid = bidService.updateBidStatus(bidId, status);
    return ResponseEntity.ok(updatedBid);
  }

  @DeleteMapping("/{bidId}")
  public ResponseEntity<Void> deleteBid(@PathVariable Long bidId) {
    log.info("Deleting bid with ID: {}", bidId);
    bidService.deleteBid(bidId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/tenderer")
  public ResponseEntity<PageDTO<BidDTO>> getBidsByTenderer(
          @RequestHeader("X-User-ID") Long tendererId,
          @PageableDefault(size = 10) Pageable pageable) {

    log.info("Getting bids for tenderer ID: {}", tendererId);
    PageDTO<BidDTO> bids = bidService.getBidsByTenderer(tendererId, pageable);
    return ResponseEntity.ok(bids);
  }

  @GetMapping("/tender/{tenderId}")
  public ResponseEntity<PageDTO<BidDTO>> getBidsByTender(
          @PathVariable Long tenderId,
          @PageableDefault(size = 10) Pageable pageable) {

    log.info("Getting bids for tender ID: {}", tenderId);
    PageDTO<BidDTO> bids = bidService.getBidsByTender(tenderId, pageable);
    return ResponseEntity.ok(bids);
  }

  @GetMapping("/tender/{tenderId}/status/{status}")
  public ResponseEntity<List<BidDTO>> getBidsByTenderAndStatus(
          @PathVariable Long tenderId,
          @PathVariable BidStatus status) {

    log.info("Getting bids for tender ID: {} with status: {}", tenderId, status);
    List<BidDTO> bids = bidService.getBidsByTenderAndStatus(tenderId, status);
    return ResponseEntity.ok(bids);
  }

  @PostMapping("/{bidId}/documents")
  public ResponseEntity<BidDTO> addDocumentToBid(
          @PathVariable Long bidId,
          @RequestParam("file") MultipartFile file,
          @RequestParam("fileName") String fileName) {

    log.info("Adding document to bid ID: {}", bidId);
    BidDTO updatedBid = bidService.addDocumentToBid(bidId, file, fileName);
    return ResponseEntity.ok(updatedBid);
  }

  @DeleteMapping("/{bidId}/documents/{documentId}")
  public ResponseEntity<Void> removeDocumentFromBid(
          @PathVariable Long bidId,
          @PathVariable Long documentId) {

    log.info("Removing document ID: {} from bid ID: {}", documentId, bidId);
    bidService.removeDocumentFromBid(bidId, documentId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/check")
  public ResponseEntity<Boolean> hasTendererBidForTender(
          @RequestParam Long tenderId,
          @RequestHeader("X-User-ID") Long tendererId) {

    log.info("Checking if tenderer ID: {} has bid for tender ID: {}", tendererId, tenderId);
    boolean hasBid = bidService.hasTendererBidForTender(tenderId, tendererId);
    return ResponseEntity.ok(hasBid);
  }
}

