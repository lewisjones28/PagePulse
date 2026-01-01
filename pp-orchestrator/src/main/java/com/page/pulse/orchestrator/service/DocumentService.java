package com.page.pulse.orchestrator.service;

import com.page.pulse.domain.entity.Document;
import com.page.pulse.orchestrator.pojo.DocumentDto;
import com.page.pulse.orchestrator.repository.DocumentRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing documents.
 *
 * @author lewisjones
 */
@Service
public class DocumentService
{
    private final DocumentRepository repository;

    /**
     * Constructs a DocumentService with the provided DocumentRepository.
     *
     * @param repository the document repository
     */
    public DocumentService( final DocumentRepository repository )
    {
        this.repository = repository;
    }

    /**
     * Retrieve a document by its external ID.
     *
     * @param externalId the external id
     * @return the Document optional
     */
    @Cacheable( "documents" )
    public Optional<Document> getDocumentByExternalId( final String externalId )
    {
        return repository.findByExternalId( externalId );
    }

    /**
     * Save a new document or update an existing one based on the DTO.
     *
     * @param dto the data transfer object
     * @return the saved or updated document
     */
    @Transactional
    @CachePut( value = "documents", key = "#dto.externalId()" )
    public Document saveOrUpdate( final DocumentDto dto )
    {
        return getDocumentByExternalId( dto.externalId() )
            .map( document -> updateDocument( document, dto ) )
            .orElseGet( () -> repository.save( buildDocument( dto ) ) );
    }

    /**
     * Update an existing document with data from a DTO.
     *
     * @param document the existing document
     * @param dto      the data transfer object with new data
     * @return the updated document
     */
    private static Document updateDocument( final Document document, final DocumentDto dto )
    {
        document.setTitle( dto.title() );
        document.setExternalId( document.getExternalId() );
        document.setStatus( dto.status() );
        document.setExternalOwnerId( dto.externalOwnerId() );
        document.getTags().clear();
        document.getTags().addAll( nonNullTags( dto.tags() ) );
        document.setDocumentLastUpdatedAt( dto.createdAt() );
        document.setDocumentLastCreatedAt( document.getDocumentLastCreatedAt() );
        return document;
    }

    /**
     * Build a new Document entity from a DTO.
     *
     * @param dto the data transfer object
     * @return the new Document entity
     */
    private static Document buildDocument( final DocumentDto dto )
    {
        return new Document( dto.externalId(), dto.externalOwnerId(), dto.title(), dto.status(),
            nonNullTags( dto.tags() ), dto.createdAt(), dto.updatedAt() );
    }

    /**
     * Ensure the list of tags is non-null.
     *
     * @param tags the list of tags
     * @return a non-null list of tags
     */
    private static List<String> nonNullTags( final List<String> tags )
    {
        return tags == null ? new ArrayList<>() : new ArrayList<>( tags );
    }

}
