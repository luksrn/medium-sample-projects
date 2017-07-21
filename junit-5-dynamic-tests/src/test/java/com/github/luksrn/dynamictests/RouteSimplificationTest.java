package com.github.luksrn.dynamictests;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class RouteSimplificationTest {

	RouterSimplification instance = new RouterSimplification();
	
	@DisplayName("Teste do algoritmo de compressão das rotas.")
	@ParameterizedTest
	@ArgumentsSource(RouteArgumentsProvider.class)	
	void testCompression(Route original, Route expected) {
	 
		Route simplifed = instance.compress(original);
		
		Assertions.assertNotNull(original);
		Assertions.assertNotNull(expected);
		//Assertions.assertEquals(expected, simplifed, () -> "Compressão inválida.");
	}
	
	static class RouteArgumentsProvider implements ArgumentsProvider {

	    @Override
	    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws IOException {
    		return Files.list(getSampleDataDir("sample-data"))
    			.map( this::mapToArguments );	        
	    }
	    
	    private Path getSampleDataDir(String dir) {
	    	URL url = Thread.currentThread().getContextClassLoader().getResource(dir);
    		Path rootDir = Paths.get( url.getPath());  
    		return rootDir;
	    }
	    
	    private Arguments mapToArguments(Path base) {
	    	Route input = readRouteFromFile(base.resolve(Paths.get("input")));
	    	Route output = readRouteFromFile(base.resolve(Paths.get("output")));

	    	return Arguments.of(input,output);
	    }
	    
	    private Route readRouteFromFile(Path f) {
    		Route r = null;
	    	try (Stream<String> lines = Files.lines(f) ){
	    		List<SpaceTimePoint> st = lines
						.map(line ->  line.split(",") )
						.map(this::mapToSpaceTimePoint)
						.collect(Collectors.toList());

	    		r = new Route();
	    		r.setPoints(st);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	
	    	return r;
	    }
	    
	    private SpaceTimePoint mapToSpaceTimePoint(String[] data) {
	    	SpaceTimePoint r = new SpaceTimePoint(); 
	    	r.setTime( Long.parseLong(data[0]) );
	    	
	    	GeographicalPoint g = new GeographicalPoint();
	    	g.setLatitude( Double.parseDouble(data[1]) );
	    	g.setLongitude( Double.parseDouble(data[2]) );
	    	
	    	r.setGeographicalPoint(g);
	    	
	    	return r;
	    }
	}
}
