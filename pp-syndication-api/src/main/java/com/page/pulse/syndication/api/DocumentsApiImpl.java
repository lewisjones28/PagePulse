package com.page.pulse.syndication.api;

import com.page.pulse.syndication.mapper.DocumentApiMapper;
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

import java.util.List;

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
     * Retrieves a list of documents.
     *
     * @param page the page number
     * @param size the page size
     * @param sort the sort criteria
     * @return ResponseEntity containing a list of DocumentApiDto
     */
    @Override
    public ResponseEntity<PagedDocumentApiDto> getDocuments( final Integer page, final Integer size,
        final List<String> sort )
    {
        final Pageable pageable = PageRequest.of( page, size, Sort.by( PaginationUtil.createSortOrder( sort ) ) );
        final Page<com.page.pulse.domain.entity.Document> documents = documentApiService.getDocuments( pageable );
        final PagedDocumentApiDto response = PaginationUtil.toPagedDto( documents, documentApiMapper::toDto );
        return ResponseEntity.ok( response );
    }
}
