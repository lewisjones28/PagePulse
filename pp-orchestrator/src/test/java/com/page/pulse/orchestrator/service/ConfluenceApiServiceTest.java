package com.page.pulse.orchestrator.service;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.page.pulse.confluence.client.ConfluenceApiClient;
import com.page.pulse.confluence.client.page.params.ConfluencePageParams;
import com.page.pulse.orchestrator.mapper.BaseDocumentMapper;
import com.page.pulse.orchestrator.pojo.Document;

/**
 * Unit tests for {@link ConfluenceApiService}.
 *
 * @author lewisjones
 */
@ExtendWith( MockitoExtension.class )
class ConfluenceApiServiceTest
{
    private static final String PAGE_1_ID = "12345";
    @Mock
    private ConfluenceApiClient confluenceApiClient;
    @Mock
    private BaseDocumentMapper<Document> documentMapper;
    @InjectMocks
    private ConfluenceApiService confluenceApiService;

    @Test
    void testCollectPagesValid()
    {
        // given
        final ConfluencePageParams params = mock( ConfluencePageParams.class );
        final JsonNode mockPagesResponse = mock( JsonNode.class );
        final JsonNode mockIdNode1 = mock( JsonNode.class );
        final JsonNode mockPageDetails1 = mock( JsonNode.class );
        final Document mockDocument1 = mock( Document.class );

        // when
        when( params.isEmpty() ).thenReturn( false );
        when( params.toMap() ).thenReturn( Map.of( "status", "current" ) );
        when( confluenceApiClient.getPages( any( Map.class ) ) ).thenReturn( mockPagesResponse );
        when( mockPagesResponse.isNull() ).thenReturn( false );
        when( mockPagesResponse.findValues( "id" ) ).thenReturn( List.of( mockIdNode1 ) );
        when( mockIdNode1.asText() ).thenReturn( PAGE_1_ID );
        when( confluenceApiClient.getPage( eq( PAGE_1_ID ), any( Map.class ) ) ).thenReturn( mockPageDetails1 );
        when( documentMapper.toDocumentList( List.of( mockPageDetails1 ) ) ).thenReturn( List.of( mockDocument1 ) );

        final List<Document> result = confluenceApiService.collectPages( params );

        // then
        assertThat( result ).hasSize( 1 ).containsExactly( mockDocument1 );
    }

    @Test
    void testCollectPagesWithNullParamsReturnsEmptyListWhenNoPages()
    {
        // given
        final JsonNode mockPagesResponse = mock( JsonNode.class );

        // when
        when( confluenceApiClient.getPages() ).thenReturn( mockPagesResponse );
        when( mockPagesResponse.isNull() ).thenReturn( true );

        final List<Document> result = confluenceApiService.collectPages( null );

        // then
        assertThat( result ).isEmpty();
    }

    @Test
    void testCollectPagesReturnsEmptyListOnException()
    {
        // given
        final ConfluencePageParams params = mock( ConfluencePageParams.class );

        // when
        when( params.isEmpty() ).thenReturn( false );
        when( params.toMap() ).thenReturn( Map.of( "status", "current" ) );
        when( confluenceApiClient.getPages( any( Map.class ) ) ).thenThrow( new RuntimeException( "API error" ) );

        final List<Document> result = confluenceApiService.collectPages( params );

        // then
        assertThat( result ).isEmpty();
    }

    @Test
    void testCollectPagesWithEmptyIdListReturnsEmptyList()
    {
        // given
        final ConfluencePageParams params = mock( ConfluencePageParams.class );
        final JsonNode mockPagesResponse = mock( JsonNode.class );

        // when
        when( params.isEmpty() ).thenReturn( false );
        when( params.toMap() ).thenReturn( Map.of( "status", "current" ) );
        when( confluenceApiClient.getPages( any( Map.class ) ) ).thenReturn( mockPagesResponse );
        when( mockPagesResponse.isNull() ).thenReturn( false );
        when( mockPagesResponse.findValues( "id" ) ).thenReturn( List.of() );
        when( documentMapper.toDocumentList( List.of() ) ).thenReturn( List.of() );

        final List<Document> result = confluenceApiService.collectPages( params );

        // then
        assertThat( result ).isEmpty();
    }

}
