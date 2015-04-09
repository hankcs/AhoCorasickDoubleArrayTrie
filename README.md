AhoCorasickDoubleArrayTrie
============

An extremely fast implementation of Aho Corasick algorithm based on Double Array Trie structure. Its speed is 1.7 to 4.5 times of naive implementations, perhaps it's the fastest implementation so far ;-)

Introduction
------------
You may heard that Aho-Corasick algorithm is fast for parsing text with a huge dictionary, for example:
* looking for certain words in texts in order to URL link or emphasize them
* adding semantics to plain text
* checking against a dictionary to see if syntactic errors were made

But most implementation use a `TreeMap<Character, State>` to store the *goto* structure, which costs `O(ln(t))` time, `t` is the largest amount of a word's common prefixes. The final complexity is `O(n * ln(t))`, absolutely `t > 2`, so `n * ln(t) > n `. The others used a `HashMap`, which wasted too much memory, and still remained slowly.

I improve it by replace the `XXXMap` to a Double Array Trie, whose time complexity is just `O(1)`, thus we get a total complexity of exactly `O(n)`, and take a perfect balance of time and memory. Yes, its speed is not related to the length or language or common prefix of the words of a dictionary.

Usage
-----
Setting up the `AhoCorasickDoubleArrayTrie` is a piece of cake:
```java
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
        AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<String>();
        acdat.build(map);
        // Test it
        final String text = "uhers";
        List<AhoCorasickDoubleArrayTrie<String>.Hit<String>> wordList = acdat.parseText(text);
```

Of course, there remains many useful methods to be discovered, feel free to try:
* Use a `Map<String, Object>` to assign a Object as value to a keyword.
* Store the `AhoCorasickDoubleArrayTrie` to disk by calling `save` method.
* Restore the `AhoCorasickDoubleArrayTrie` from disk by calling `load` method.

In other situations you probably do not need a huge wordList, then please try this:

```java
        acdat.parseText(text, new AhoCorasickDoubleArrayTrie.IHit<String>()
        {
            @Override
            public void hit(int begin, int end, String value)
            {
                System.out.printf("[%d:%d]=%s\n", begin, end, value);
            }
        });
```

or a lambda function
```
        acdat.parseText(text, (begin, end, value) -> {
            System.out.printf("[%d:%d]=%s\n", begin, end, value);
        });
```

Comparison
-----
I compared my AhoCorasickDoubleArrayTrie with robert-bor's aho-corasick, ACDAT represents for AhoCorasickDoubleArrayTrie and Naive repesents for aho-corasick, the result is :
```
Parsing English document which contains 3409283 characters, with a dictionary of 127142 words.
               	Naive          	ACDAT          
time           	554            	290            
char/s         	6153940.43     	11756148.28    
rate           	1.00           	1.91           
===========================================================================
Parsing Chinese document which contains 1290573 characters, with a dictionary of 146047 words.
               	Naive          	ACDAT          
time           	269            	56             
char/s         	4797669.14     	23045946.43    
rate           	1.00           	4.80           
===========================================================================
```

In English test, AhoCorasickDoubleArrayTrie is 1.87 times faster. When it comes to Chinese, AhoCorasickDoubleArrayTrie is 4.35 times faster.
Feel free to re-run this test in TestAhoCorasickDoubleArrayTrie, the test data is ready for you.


License
-------
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
