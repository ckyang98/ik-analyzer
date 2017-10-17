IK分词器，修改后兼容4.10.3
配置方法：
```
 <fieldType name="text_ik" class="solr.TextField">
    <analyzer type="query">
        <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="false"/>
        <filter class="solr.LowerCaseFilterFactory"/>
    </analyzer>
    <analyzer type="index">
       <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="fasle"/>
       <filter class="solr.LowerCaseFilterFactory"/>
   </analyzer>
</fieldType>
```
其中useSmart是否启用智能分词