# Guia de GitHub - ControleGastos

## Status Atual
✅ Repositório Git inicializado
✅ Primeiro commit realizado

## Próximos Passos para Enviar ao GitHub

### 1. Criar um Repositório no GitHub
1. Acesse [GitHub](https://github.com)
2. Faça login na sua conta
3. Clique no botão **"+"** no canto superior direito
4. Selecione **"New repository"**
5. Preencha os dados:
   - **Repository name:** `controle-gastos-api` (ou o nome que preferir)
   - **Description:** "API REST para controle de gastos com Spring Boot, Spring Security e JWT"
   - **Visibilidade:** Escolha entre Public ou Private
   - ⚠️ **NÃO marque** "Initialize this repository with a README" (já temos arquivos locais)
6. Clique em **"Create repository"**

### 2. Conectar Seu Repositório Local ao GitHub

Após criar o repositório no GitHub, você verá instruções. Use os comandos abaixo no terminal:

```bash
# Entre no diretório do projeto (se ainda não estiver)
cd /Users/vitoralmeida/Documents/SPTech/3semestre/ProjetoExtensao/Backend/CarePlus/careplus/ControleGastos

# Adicione o repositório remoto (substitua SEU_USUARIO pelo seu username do GitHub)
git remote add origin https://github.com/SEU_USUARIO/controle-gastos-api.git

# Verifique se o remote foi adicionado corretamente
git remote -v

# Envie o código para o GitHub (primeira vez)
git push -u origin master
```

**Alternativa com SSH (se você já configurou chaves SSH):**
```bash
git remote add origin git@github.com:SEU_USUARIO/controle-gastos-api.git
git push -u origin master
```

### 3. Autenticação

O GitHub não aceita mais senha via HTTPS. Você tem duas opções:

#### Opção A: Personal Access Token (PAT)
1. Vá em GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Clique em "Generate new token (classic)"
3. Dê um nome descritivo (ex: "MacBook Air")
4. Selecione o escopo: **repo** (full control of private repositories)
5. Clique em "Generate token"
6. **COPIE O TOKEN** (você não poderá vê-lo novamente!)
7. Quando executar `git push`, use o token como senha

#### Opção B: SSH Keys (Recomendado)
```bash
# Gerar chave SSH (se não tiver uma)
ssh-keygen -t ed25519 -C "seu_email@example.com"

# Copiar a chave pública
cat ~/.ssh/id_ed25519.pub

# Adicione a chave no GitHub:
# GitHub → Settings → SSH and GPG keys → New SSH key
# Cole o conteúdo do arquivo .pub
```

### 4. Comandos Git Úteis para o Dia a Dia

```bash
# Ver status dos arquivos
git status

# Adicionar arquivos modificados
git add .

# Fazer commit das mudanças
git commit -m "Descrição das alterações"

# Enviar para o GitHub
git push

# Baixar mudanças do GitHub
git pull

# Ver histórico de commits
git log --oneline

# Criar uma nova branch
git checkout -b nome-da-branch

# Trocar de branch
git checkout nome-da-branch

# Ver branches existentes
git branch
```

### 5. Estrutura de Commits Recomendada

Use mensagens de commit claras e descritivas:

```bash
# Bons exemplos:
git commit -m "feat: adiciona endpoint de relatórios de gastos"
git commit -m "fix: corrige cálculo de total por categoria"
git commit -m "docs: atualiza documentação da API"
git commit -m "refactor: melhora validação de dados no GastoService"
git commit -m "chore: atualiza dependências do Spring Boot"
```

### 6. Arquivo .gitignore

O projeto já possui um `.gitignore` que exclui:
- `/target/` - Arquivos compilados
- `.DS_Store` - Arquivos do macOS
- Arquivos de configuração de IDEs
- Banco de dados H2

### 7. Dicas Importantes

- ✅ Sempre faça `git pull` antes de começar a trabalhar
- ✅ Faça commits frequentes com mensagens descritivas
- ✅ Não commite arquivos sensíveis (senhas, tokens, etc.)
- ✅ Use branches para novas features
- ✅ Revise suas mudanças antes de fazer push

### 8. Trabalhando em Equipe

```bash
# Criar uma branch para nova feature
git checkout -b feature/adiciona-relatorios

# Fazer suas alterações e commit
git add .
git commit -m "feat: implementa relatórios mensais"

# Enviar a branch para o GitHub
git push -u origin feature/adiciona-relatorios

# No GitHub, crie um Pull Request para review
```

### 9. Comandos Rápidos para Copiar

```bash
# Configurar usuário Git (se ainda não fez)
git config --global user.name "Seu Nome"
git config --global user.email "seu_email@example.com"

# Ver configuração atual
git config --list

# Adicionar remote (substitua SEU_USUARIO)
git remote add origin https://github.com/SEU_USUARIO/controle-gastos-api.git

# Primeira vez enviando ao GitHub
git push -u origin master

# Próximas vezes (após commits)
git add .
git commit -m "Sua mensagem"
git push
```

### 10. Recursos Adicionais

- [Documentação Oficial do Git](https://git-scm.com/doc)
- [GitHub Guides](https://guides.github.com/)
- [Git Cheat Sheet](https://education.github.com/git-cheat-sheet-education.pdf)

---

## Resumo do Status do Projeto

**Commit realizado:** ✅
- 46 arquivos incluídos
- 3761 linhas adicionadas
- Mensagem: "Initial commit: Sistema de controle de gastos com Spring Boot, Spring Security e JWT"

**Próximo passo:** Criar repositório no GitHub e fazer o push! 🚀

