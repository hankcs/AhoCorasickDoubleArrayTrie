AhoCorasickDoubleArrayTrie
============
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.hankcs/aho-corasick-double-array-trie/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.hankcs/aho-corasick-double-array-trie/)
[![GitHub release](https://img.shields.io/github/release/hankcs/AhoCorasickDoubleArrayTrie.svg)](https://github.com/hankcs/AhoCorasickDoubleArrayTrie/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

An extremely fast implementation of Aho Corasick algorithm based on Double Array Trie structure. Its speed is 5 to 9 times of naive implementations, perhaps it's the fastest implementation so far ;-)

Introduction
------------
You may heard that Aho-Corasick algorithm is fast for parsing text with a huge dictionary, for example:
* looking for certain words in texts in order to URL link or emphasize them
* adding semantics to plain text
* checking against a dictionary to see if syntactic errors were made

But most implementation use a `TreeMap<Character, State>` to store the *goto* structure, which costs `O(lg(t))` time, `t` is the largest amount of a word's common prefixes. The final complexity is `O(n * lg(t))`, absolutely `t > 2`, so `n * lg(t) > n `. The others used a `HashMap`, which wasted too much memory, and still remained slowly.

I improved it by replacing the `XXXMap` to a Double Array Trie, whose time complexity is just `O(1)`, thus we get a total complexity of exactly `O(n)`, and take a perfect balance of time and memory. Yes, its speed is not related to the length or language or common prefix of the words of a dictionary.

This implementation has been widely used in my [HanLP: Han Language Processing](https://github.com/hankcs/HanLP) package. I hope it can serve as a common data structure library in projects handling text or NLP task.

Dependency
----------
Include this dependency in your POM. Be sure to check for the latest version in Maven Central.

```xml
<dependency>
  <groupId>com.hankcs</groupId>
  <artifactId>aho-corasick-double-array-trie</artifactId>
  <version>1.2.2</version>
</dependency>
```
or include this dependency in your build.gradle.kts
```kotlin
implementation("com.hankcs:aho-corasick-double-array-trie:1.2.2")
```

Usage
-----
Setting up the `AhoCorasickDoubleArrayTrie` is a piece of cake:

```java
        // Collect test data set
        TreeMap<String, String> map = new TreeMap<String, String>();
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
        List<AhoCorasickDoubleArrayTrie.Hit<String>> wordList = acdat.parseText(text);
```

Of course, there remains many useful methods to be discovered, feel free to try:
* Use a `Map<String, SomeObject>` to assign a `SomeObject` as value to a keyword.
* Store the `AhoCorasickDoubleArrayTrie` to disk by calling `save` method.
* Restore the `AhoCorasickDoubleArrayTrie` from disk by calling `load` method.
* Use it in concurrent code. `AhoCorasickDoubleArrayTrie` is thread safe after `build` method

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
I compared my AhoCorasickDoubleArrayTrie with robert-bor's aho-corasick, ACDAT represents for AhoCorasickDoubleArrayTrie and Naive represents for aho-corasick, the result is :

```
Parsing English document which contains 3409283 characters, with a dictionary of 127142 words.
               	Naive          	ACDAT
time           	607            	102
char/s         	5616611.20     	33424343.14
rate           	1.00           	5.95
===========================================================================
Parsing Chinese document which contains 1290573 characters, with a dictionary of 146047 words.
               	Naive          	ACDAT
time           	319            	35
char/s         	2609156.74     	23780600.00
rate           	1.00           	9.11
===========================================================================
```

In English test, AhoCorasickDoubleArrayTrie is 5 times faster. When it comes to Chinese, AhoCorasickDoubleArrayTrie is 9 times faster.
This test is conducted under i7 2.0GHz, -Xms512m -Xmx512m -Xmn256m. Feel free to re-run this test in TestAhoCorasickDoubleArrayTrie, the test data is ready for you.

Thanks
-----
This project is inspired by [aho-corasick](https://github.com/robert-bor/aho-corasick) and [darts-clone-java](https://github.com/hiroshi-manabe/darts-clone-java).
Many thanks!

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


