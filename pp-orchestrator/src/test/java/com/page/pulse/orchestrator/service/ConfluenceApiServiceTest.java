package com.page.pulse.orchestrator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.page.pulse.orchestrator.pojo.DocumentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.page.pulse.confluence.client.ConfluenceApiClient;
import com.page.pulse.confluence.client.page.params.ConfluencePageParams;
import com.page.pulse.orchestrator.mapper.BaseDocumentMapper;

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
    private BaseDocumentMapper<DocumentDto> documentMapper;
    @InjectMocks
    private ConfluenceApiService confluenceApiService;

    @Test
    void testCollectPagesValid()
    {
        // given
        final ConfluencePageParams params = mock( ConfluencePageParams.class );
        final DocumentDto mockDocumentDto1 = mock( DocumentDto.class );

        // when
        when( params.isEmpty() ).thenReturn( false );
        when( params.toMap() ).thenReturn( Map.of( "status", "current" ) );
        when( confluenceApiClient.getPages( any( Map.class ) ) ).thenReturn( "{\"results\":[{\"id\":\"12345\"}]}" );
        when( confluenceApiClient.getPage( eq( PAGE_1_ID ), any( Map.class ) ) ).thenReturn( "{\"id\":\"12345\"}" );
        when( documentMapper.toDocumentList( org.mockito.ArgumentMatchers.<List<JsonNode>>any() ) ).thenAnswer( invocation ->
        {
            final List<?> nodes = invocation.getArgument( 0 );
            return nodes.size() == 1 ? List.of( mockDocumentDto1 ) : List.of();
        } );

        final List<DocumentDto> result = confluenceApiService.collectPages( params );

        // then
        assertThat( result ).hasSize( 1 ).containsExactly( mockDocumentDto1 );
    }

    @Test
    void testCollectPagesWithNullParamsReturnsEmptyListWhenNoPages()
    {
        // given
        when( confluenceApiClient.getPages() ).thenReturn( "null" );

        final List<DocumentDto> result = confluenceApiService.collectPages( null );

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

        final List<DocumentDto> result = confluenceApiService.collectPages( params );

        // then
        assertThat( result ).isEmpty();
    }

    @Test
    void testCollectPagesWithEmptyIdListReturnsEmptyList()
    {
        // given
        final ConfluencePageParams params = mock( ConfluencePageParams.class );

        // when
        when( params.isEmpty() ).thenReturn( false );
        when( params.toMap() ).thenReturn( Map.of( "status", "current" ) );
        when( confluenceApiClient.getPages( any( Map.class ) ) ).thenReturn( "{\"results\":[]}" );
        when( documentMapper.toDocumentList( List.of() ) ).thenReturn( List.of() );

        final List<DocumentDto> result = confluenceApiService.collectPages( params );

        // then
        assertThat( result ).isEmpty();
    }

}
