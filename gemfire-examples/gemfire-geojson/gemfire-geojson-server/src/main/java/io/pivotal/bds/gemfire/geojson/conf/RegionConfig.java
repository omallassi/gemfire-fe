package io.pivotal.bds.gemfire.geojson.conf;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.PartitionAttributes;
import org.apache.geode.cache.PartitionAttributesFactory;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.RegionFactory;
import org.apache.geode.cache.RegionShortcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;

import io.pivotal.bds.gemfire.geojson.data.Boundary;
import io.pivotal.bds.gemfire.geojson.listener.BucketMappingChangeCacheListener;
import io.pivotal.bds.gemfire.geojson.listener.NotifyPartitionListener;
import io.pivotal.bds.gemfire.geojson.writer.GeoJsonCacheWriter;

@Configuration
public class RegionConfig {

    private static final Logger LOG = LoggerFactory.getLogger(RegionConfig.class);

    @Bean
    public static Region<Integer, String> bucketMappingRegion(Cache cache, Boundary rootBoundary) {
        LOG.info("bucketMappingRegion");
        RegionFactory<Integer, String> rf = cache.createRegionFactory(RegionShortcut.REPLICATE);
        rf.addCacheListener(new BucketMappingChangeCacheListener(rootBoundary));
        return rf.create("bucketMapping");
    }

    @Bean
    public static Region<Integer, String> jsonFeatureRegion(Cache cache, Boundary rootBoundary, MetricRegistry registry) {
        LOG.info("jsonFeatureRegion");
        RegionFactory<Integer, String> rf = cache.createRegionFactory(RegionShortcut.PARTITION);
        rf.setCacheWriter(new GeoJsonCacheWriter(rootBoundary, registry));
        
        PartitionAttributesFactory<Integer, String> paf = new PartitionAttributesFactory<>();
        paf.addPartitionListener(new NotifyPartitionListener());
        PartitionAttributes<Integer, String> pa = paf.create();
        
        rf.setPartitionAttributes(pa);
        
        return rf.create("jsonFeature");
    }

}
