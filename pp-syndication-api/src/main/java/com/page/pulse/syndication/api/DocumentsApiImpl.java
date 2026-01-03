package com.page.pulse.syndication.api;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.domain.entity.DocumentRuleViolation;
import com.page.pulse.syndication.mapper.DocumentApiMapper;
import com.page.pulse.syndication.model.DocumentApiDto;
import com.page.pulse.syndication.model.PagedDocumentApiDto;
import com.page.pulse.syndication.service.DocumentApiService;
import com.page.pulse.syndication.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link DocumentsApi} interface.
 *
 * @author lewisjones
 */
@RestController
@RequiredArgsConstructor
public class DocumentsApiImpl implements DocumentsApi
{

    private final DocumentApiService documentApiService;
    private final DocumentApiMapper documentApiMapper = DocumentApiMapper.INSTANCE;

    /**
     * Retrieves a list of documents with their rule violations.
     *
     * @param page the page number
     * @param size the page size
     * @param sort the sort criteria
     * @return ResponseEntity containing a list of DocumentApiDto with rule violations
     */
    @Override
    public ResponseEntity<PagedDocumentApiDto> getDocuments( final Integer page, final Integer size,
        final List<String> sort )
    {
        final Pageable pageable = PageRequest.of( page, size, Sort.by( PaginationUtil.createSortOrder( sort ) ) );
        final Page<Document> documents = documentApiService.getDocuments( pageable );

        // Get rule violations for all documents on this page
        final List<Document> documentList = documents.getContent();
        final Map<Document, List<DocumentRuleViolation>> violationsMap =
            documentApiService.getViolationsForDocuments( documentList );

        // Map documents with their violations
        final List<DocumentApiDto> documentDtos = documentList.stream()
            .map( document -> {
                final List<DocumentRuleViolation> violations = violationsMap.getOrDefault( document, Collections.emptyList() );
                return documentApiMapper.toDtoWithViolations( document, violations );
            } )
            .toList();

        final PagedDocumentApiDto response = PaginationUtil.toPagedDtoWithMappedContent( documents, documentDtos );
        return ResponseEntity.ok( response );
    }
}
