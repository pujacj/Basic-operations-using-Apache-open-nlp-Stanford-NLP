package com.hm

import java.io.{FileInputStream}
import opennlp.tools.cmdline.parser.ParserTool
import opennlp.tools.namefind.{NameFinderME, TokenNameFinderModel}
import opennlp.tools.parser.{ParserFactory, ParserModel}
import opennlp.tools.sentdetect.{SentenceDetectorME, SentenceModel}
import opennlp.tools.tokenize.{TokenizerME, TokenizerModel}
import com.chaoticity.dependensee._
import edu.stanford.nlp.parser.lexparser.LexicalizedParser
import edu.stanford.nlp.process.CoreLabelTokenFactory
import edu.stanford.nlp.process.PTBTokenizer
import edu.stanford.nlp.trees.PennTreebankLanguagePack

import java.io.StringReader

import edu.stanford.nlp.semgraph.{SemanticGraph}

/**
  * Created by pooja on 27/3/17.
  */
object Test extends App {

  var par = "Bob brought the pizza to Alice ."
  println("The Sentences are :")
  sentenceDetector(par)
  println("-------------------------------------------------------------")
  println("The tokens are :")
  var tokens = tokenizer(par)
  tokens.foreach(i => {
    println(i)
  })
  println("-------------------------------------------------------------")
  println("The names are :")
  nameFinder(par)
  println("-------------------------------------------------------------")
  println("The locations are :")
  locFinder(par)
  println("-------------------------------------------------------------")
  println("The parsed sentence is :")
  parser(par)
  println("-------------------------------------------------------------")
  dependencies(par)


  def sentenceDetector(par: String) = {
    val a = new FileInputStream("/home/pooja/Downloads/en-sent.bin")
    val model = new SentenceModel(a)
    val sdetector = new SentenceDetectorME(model)
    val sentences: Array[String] = sdetector.sentDetect(par)

    sentences.foreach(i => {
      println(i)
    })
    a.close()
  }

  def tokenizer(par: String) = {
    val a = new FileInputStream("/home/pooja/Downloads/en-token.bin")
    val model = new TokenizerModel(a)
    val tokenizer = new TokenizerME(model)
    val sentences: Array[String] = tokenizer.tokenize(par)
    a.close()
    sentences
  }

  def nameFinder(par: String) = {
    val a = new FileInputStream("/home/pooja/Downloads/en-ner-person.bin")
    val model = new TokenNameFinderModel(a)
    a.close()

    val nameFinder = new NameFinderME(model)
    val sentence: Array[String] = tokenizer(par)
    val nameSpans = nameFinder.find(sentence)
    nameSpans.foreach(i => {
      if (i.length() > 1)
        println(sentence(i.getStart) + "\n" + sentence(i.getEnd - 1))
      else
        println(sentence(i.getStart))

    })
  }


  def locFinder(par: String) = {
    val a = new FileInputStream("/home/pooja/Downloads/en-ner-location.bin")
    val model = new TokenNameFinderModel(a)
    a.close()

    val nameFinder = new NameFinderME(model)
    val sentence: Array[String] = tokenizer(par)
    val nameSpans = nameFinder.find(sentence)
    nameSpans.foreach(i => {
      if (i.length() > 1)
        println(sentence(i.getStart) + "\n" + sentence(i.getEnd - 1))
      else
        println(sentence(i.getStart))

    })
  }

  def parser(par: String) = {
    val a = new FileInputStream("/home/pooja/Downloads/en-parser-chunking.bin")
    val model = new ParserModel(a)
    val parser = ParserFactory.create(model)

    val topParses = ParserTool.parseLine(par, parser, 1)
    for (p <- topParses)
      p.show()
    a.close()

  }

  def dependencies(par:String) = {

        //val text = "Bob brought the pizza to Alice ."
        println(par)
        val tlp = new PennTreebankLanguagePack() //Specifies the treebank/language specific components needed for parsing the English Penn Treebank.
        val gsf = tlp.grammaticalStructureFactory()
        val lp = LexicalizedParser.loadModel("src/test/resources/englishPCFG.ser.gz")
        //lp.setOptionFlags(new String[]{"-maxLength", "500", "-retainTmpSubcategories"})
        val tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "")
        val wordList = tokenizerFactory.getTokenizer(new StringReader(par)).tokenize()
        val tree = lp.apply(wordList)
        val gs = gsf.newGrammaticalStructure(tree)
        val tdl = gs.typedDependenciesCCprocessed(true)
        println("The dependencies are :")
        println(tdl)
        println("-------------------------------------------------------------")
        com.chaoticity.dependensee.Main.writeImage(tree,tdl, "pooja.png",2)
        val semgraph = new SemanticGraph(tdl)
        println("The semantic graph is :")
        println(semgraph)

         val rw = semgraph.getFirstRoot
         println("::"+rw)
       println("::"+semgraph.getChildren(rw))
       println(semgraph.getLeafVertices())

/*
//    basic         gs.typedDependencies()
//    nonCollapsed  gs.allTypedDependencies()
//    collapsed     gs.typedDependenciesCollapsed(true)
//    CCPropagated  gs.typedDependenciesCCprocessed()
//    tree          gs.typedDependenciesCollapsedTree()
  */

  }
}
