package br.com.inatel.relatorio;

import br.com.inatel.utils.ManipulacaoDeArquivos;
import org.sikuli.script.FindFailed;
import org.sikuli.script.App;
import org.sikuli.script.Screen;

public class Qgis extends ManipulacaoDeArquivos {
    static Screen tela = new Screen();

    public static void gerarRelatorio(String[] portadoras, String site, String cidade, char siteSeisSetores, String pasta) {
        String uf = site.substring(site.length() - 2);
        String imgQgis = pasta + "\\ferramentas\\qgis\\";
        String pastaSite = pasta + "\\relatorios\\" + site;

        String pastaPortadora;

        String caminhoArquivoZip;
        String pastaDestino;

        String ssvReportProcessing;
        String ssvReportGenerator;

        switch (siteSeisSetores) {
            case 's' -> {
                System.out.println("\n██████████ 6 SETORES ██████████");

                pastaPortadora = criarPasta(pastaSite, "6_setores");

                ssvReportProcessing = pasta + "\\ferramentas\\templates\\SRB_VIVO_six_sectors_allfreq\\1 - PythonCode\\SSV_Report Processing.qgz";
                ssvReportGenerator = pasta + "\\ferramentas\\templates\\SRB_VIVO_six_sectors_allfreq\\SSV_ReportGenerator_all_frequencies.qgz";

                caminhoArquivoZip = pasta + "\\output\\output_" + site + ".zip";
                pastaDestino = pasta + "\\ferramentas\\templates\\SRB_VIVO_six_sectors_allfreq\\1 - PythonCode\\output";

                editarTabelaSeisSetores(site, cidade, uf, pasta);
                extrairArquivoCompactado(caminhoArquivoZip, pastaDestino);

                executarQgis(ssvReportProcessing, ssvReportGenerator, imgQgis, pastaPortadora);
            }
            case 'n' -> {
                for (String portadora : portadoras) {
                    System.out.println("\n██████████ " + portadora + " ██████████");

                    pastaPortadora = criarPasta(pastaSite, portadora);
                    System.out.println(pastaPortadora);
                    ssvReportProcessing = pasta + "\\ferramentas\\templates\\01 - SRB_Template\\" + portadora + " Mhz\\1 - PythonCode\\SSV_Report Processing_" + portadora + "Mhz.qgz";
                    ssvReportGenerator = pasta + "\\ferramentas\\templates\\01 - SRB_Template\\" + portadora + " Mhz\\SSV_ReportGenerator_" + portadora + "Mhz.qgz";
                    caminhoArquivoZip = pasta + "\\output\\output_" + site + ".zip";
                    pastaDestino = pasta + "\\ferramentas\\templates\\01 - SRB_Template\\" + portadora + " Mhz\\1 - PythonCode\\output";

                    editarTabela(portadora, site, cidade, uf, pasta);
                    extrairArquivoCompactado(caminhoArquivoZip, pastaDestino);

                    executarQgis(ssvReportProcessing, ssvReportGenerator, imgQgis, pastaPortadora);
                }
            }
        }
    }

    private static void executarQgis(String ssvReportProcessing, String ssvReportGenerator, String imgQgis, String pastaPortadora) {
        try {
            App.open(ssvReportProcessing);
            tela.wait(imgQgis + "1-buscar.png", 1200);
            tela.click(imgQgis + "1-buscar.png");
            tela.type("SSV_PackLayers_v2");
            tela.wait(imgQgis + "2-SSV_PackLayers_v2.png", 1200);
            tela.doubleClick(imgQgis + "2-SSV_PackLayers_v2.png");
            tela.wait(imgQgis + "3-processado.png", 1200);
            tela.click(imgQgis + "sair.png");

            App.open(ssvReportGenerator);
            Thread.sleep(5000);
            tela.wait(imgQgis + "4-projeto.png", 1200);
            tela.click(imgQgis + "4-projeto.png");
            tela.wait(imgQgis + "5-layouts.png", 1200);
            tela.click(imgQgis + "5-layouts.png");
            tela.click(imgQgis + "6-SSV_Report.png");
            Thread.sleep(5000);
            tela.wait(imgQgis + "7-mapa.png", 1200);
            tela.click(imgQgis + "7-mapa.png");
            tela.wait(imgQgis + "8-atlas.png", 1200);
            tela.click(imgQgis + "8-atlas.png");
            tela.wait(imgQgis + "9-exportar_como_imagem.png", 1200);
            tela.click(imgQgis + "9-exportar_como_imagem.png");
            tela.wait(imgQgis + "10-pasta", 1200);
            tela.type(pastaPortadora);
            tela.click(imgQgis + "11-selecionar_pasta.png");
            tela.doubleClick(imgQgis + "12-dpi.png");
            tela.type("600");
            tela.click(imgQgis + "13-salvar.png");
            tela.wait(imgQgis + "14-sucesso", 1200);

            Thread.sleep(10000);
            tela.doubleClick(imgQgis + "sair.png");
            Thread.sleep(10000);
            tela.wait(imgQgis + "sair_vermelho.png", 1200);
            tela.click(imgQgis + "sair_vermelho.png");
            Thread.sleep(5000);
            tela.wait(imgQgis + "15-descartar", 1200);
            tela.click(imgQgis + "15-descartar");
            Thread.sleep(15000);

        } catch (FindFailed e) {
            System.out.println("IMAGEM_NAO_ENCONTRADA_NA_TELA");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}