# 🐉 RPG App Ficha

Um aplicativo Android moderno e intuitivo para gerenciamento de fichas de personagens de RPG, focado em alta performance e estética visual premium.

## 🚀 Funcionalidades Principais

- **Múltiplos Sistemas de Jogo:** Suporte dedicado para diferentes modos, incluindo:
    - 🗡️ **Fantasia (Tormenta20):** Implementação completa das regras "Jogo do Ano", com cálculos automáticos de Defesa, Perícias e Testes de Resistência.
    - 🤠 **Velho Oeste:** Ficha temática para aventuras de bangue-bangue.
    - 🧬 **Assimilação:** Sistema customizado para cenários futuristas/sci-fi.
- **Gerenciamento de Recursos:** Acompanhamento em tempo real de Vida (PV), Mana (PM), XP e Dinheiro.
- **Cálculos Automáticos:** O app calcula modificadores de atributos, bônus de nível e totais de defesa baseados em itens equipados.
- **Consumo Inteligente de PM:** Dedução automática de Pontos de Mana ao usar magias ou habilidades, com validação de recursos.
- **Sistema de Dados Avançado:**
    - Rolagens rápidas com ícones geométricos (d4, d6, d8, d10, d12, d20).
    - Lógica de Vantagem/Maior Valor em rolagens customizadas (ex: 2d10 pega o maior).
    - Histórico de rolagens detalhado.

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Kotlin
- **Interface:** Jetpack Compose (Material 3)
- **Arquitetura:** MVVM (Model-View-ViewModel)
- **Banco de Dados:** Room Persistence Library (SQL local)
- **Persistência:** StateFlow e Coroutines para fluxos de dados reativos.
- **UI/UX:** Design modernista com suporte a temas dinâmicos e modo Dark/Light.

## 📐 Regras de Tormenta20 (Modo Fantasia)

O modo Fantasia foi rigorosamente adaptado para as regras do T20 JDA:
- **Atributos:** Valores inseridos são os modificadores diretos (padrão 0).
- **Defesa/Perícias:** Fórmula `10 + Metade do Nível + Modificador de Atributo + Bônus`.
- **Treinamento:** Bônus escalonado de perícias (`+2`, `+4`, `+6` por nível).
- **Magias:** Cálculo automático de CD (Dificuldade) baseado no atributo-chave.

## 📦 Como Instalar

1. Clone o repositório.
2. Abra no **Android Studio (Koala ou superior)**.
3. Sincronize o Gradle.
4. Execute no seu dispositivo ou emulador (API 26+ recomendado).

---
*Desenvolvido para proporcionar a melhor experiência de mesa aos jogadores de RPG.*
