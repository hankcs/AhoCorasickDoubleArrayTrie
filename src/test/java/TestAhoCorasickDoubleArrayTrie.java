/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2015/4/6 12:42</create-date>
 *
 * <copyright file="TestAhoCorasickDoubleArrayTrie.java" company="�Ϻ���ԭ��Ϣ�Ƽ����޹�˾">
 * Copyright (c) 2003-2014, �Ϻ���ԭ��Ϣ�Ƽ����޹�˾. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact �Ϻ���ԭ��Ϣ�Ƽ����޹�˾ to get more information.
 * </copyright>
 */

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import junit.framework.TestCase;
import org.ahocorasick.trie.Trie;
import org.ardverk.collection.PatriciaTrie;
import org.ardverk.collection.StringKeyAnalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author hankcs
 */
public class TestAhoCorasickDoubleArrayTrie extends TestCase
{
    public void testBuildAndParseSimply() throws Exception
    {
        // Collect test data set
        Map<String, String> map = new TreeMap<String, String>();
        String[] keyArray = new String[]
                {
                        "hers",
                        "his",
                        "she",
                        "he"
                };
        for (String key : keyArray)
        {
            map.put(key, key);
        }
        // Build an AhoCorasickDoubleArrayTrie
        AhoCorasickDoubleArrayTrie<String> act = new AhoCorasickDoubleArrayTrie<String>();
        act.build(map);
        // Test it
        final String text = "uhers";
        act.parseText(text, new AhoCorasickDoubleArrayTrie.IHit<String>()
        {
            @Override
            public void hit(int begin, int end, String value)
            {
                System.out.printf("[%d:%d]=%s\n", begin, end, value);
                assertEquals(text.substring(begin, end), value);
            }
        });
        List<AhoCorasickDoubleArrayTrie<String>.Hit<String>> segmentList = act.parseText(text);
        System.out.println(segmentList);
    }

    private String loadText(String path) throws IOException
    {
        StringBuilder sbText = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)));
        String line;
        while ((line = br.readLine()) != null)
        {
            sbText.append(line).append("\n");
        }
        br.close();

        return sbText.toString();
    }

    private Set<String> loadDictionary(String path) throws IOException
    {
        Set<String> dictionary = new TreeSet<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(
            path)));
        String line;
        while ((line = br.readLine()) != null)
        {
            dictionary.add(line);
        }
        br.close();

        return dictionary;
    }

    private void runTest(String dictionaryPath, String textPath) throws IOException
    {
        Set<String> dictionary = loadDictionary(dictionaryPath);
        String text = loadText(textPath);

        // Build a ahoCorasickNaive implemented by robert-bor
        Trie ahoCorasickNaive = new Trie();
        for (String word : dictionary)
        {
            ahoCorasickNaive.addKeyword(word);
        }
        ahoCorasickNaive.parseText("");

        // Build a AhoCorasickDoubleArrayTrie implemented by hankcs
        AhoCorasickDoubleArrayTrie<String> ahoCorasickDoubleArrayTrie = new AhoCorasickDoubleArrayTrie<String>();
        Map<String, String> dictionaryMap = new TreeMap<String, String>();
        for (String word : dictionary)
        {
            dictionaryMap.put(word, word);  // we use the same text as the property of a word
        }
        ahoCorasickDoubleArrayTrie.build(dictionaryMap);

        //added by qibaoyuan,Build a patrical trie
        org.ardverk.collection.Trie<String, String> patriciaTrie = new PatriciaTrie<String, String>(StringKeyAnalyzer.INSTANCE);
        int maxKeyword=0;
        for (String word : dictionary)
        {
            if(word.length()>maxKeyword)maxKeyword=word.length();
            patriciaTrie.put(word, word);
        }

        // Let's test the speed of the two Aho-Corasick automata
        long start = System.currentTimeMillis();
        System.out.printf("Parsing document which contains %d characters, with a dictionary of %d words.\n", text.length(), dictionary.size());
        ahoCorasickNaive.parseText(text);
        long costTimeNaive = System.currentTimeMillis() - start;
        start = System.currentTimeMillis();
        ahoCorasickDoubleArrayTrie.parseText(text);
        long costTimeACDAT = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        for (int i=0;i<text.length();i++){
            for(int j=i+1;j<i+maxKeyword & j<text.length();j++){
                patriciaTrie.selectKey(text.substring(i,j));
            }
        }
        long costTimePatricia = System.currentTimeMillis() - start;

        System.out.printf("%-15s\t%-15s\t%-15s\t%-15s\n", "", "Naive", "ACDAT", "Patricia");
        System.out.printf("%-15s\t%-15d\t%-15s\t%-15d\n", "time", costTimeNaive, costTimeACDAT, costTimePatricia);
        System.out.printf("%-15s\t%-15.2f%-15s\t\t%-15.2f\n", "char/s", (text.length() / (costTimeNaive / 1000.0)), (text.length() / (costTimeACDAT / 1000.0)), (text.length() / (costTimePatricia / 1000.0)));
        System.out.printf("%-15s\t%-15.2f%-15s\t\t%-15.2f\n", "rate", 1.0, costTimeNaive / (double) costTimeACDAT,costTimeNaive / (double) costTimePatricia);
        System.out.println("===========================================================================");
    }

    public void testBenchmark() throws Exception
    {
        runTest("en/dictionary.txt", "en/text.txt");
        runTest("cn/dictionary.txt", "cn/text.txt");
    }
}
