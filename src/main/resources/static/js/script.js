document.addEventListener('DOMContentLoaded', function() {
    // Configurar data mínima para agendamentos (apenas datas futuras)
    const dataHoraInput = document.getElementById('dataHora');
    if (dataHoraInput) {
        const now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        dataHoraInput.min = now.toISOString().slice(0, 16);
    }

    // Confirmação para exclusões
    const deleteLinks = document.querySelectorAll('a[href*="/deletar/"]');
    deleteLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            if (!confirm('Tem certeza que deseja deletar este item?')) {
                e.preventDefault();
            }
        });
    });

    // Auto-hide alerts após 5 segundos
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => {
                alert.remove();
            }, 500);
        }, 5000);
    });

    // Validação de horário comercial
    if (dataHoraInput) {
        dataHoraInput.addEventListener('change', function() {
            const selectedDate = new Date(this.value);
            const dayOfWeek = selectedDate.getDay();
            const hour = selectedDate.getHours();

            // Verificar se é domingo (0) ou fora do horário comercial (8h-18h)
            if (dayOfWeek === 0) {
                alert('A barbearia não funciona aos domingos!');
                this.value = '';
                return;
            }

            if (hour < 8 || hour >= 18) {
                alert('Horário de funcionamento: 08:00 às 18:00');
                this.value = '';
                return;
            }
        });
    }
});

// Função para formatar telefone
function formatTelefone(input) {
    let value = input.value.replace(/\D/g, '');
    value = value.replace(/(\d{2})(\d)/, '($1) $2');
    value = value.replace(/(\d{5})(\d)/, '$1-$2');
    input.value = value;
}

// Aplicar máscara de telefone se existir o campo
document.addEventListener('DOMContentLoaded', function() {
    const telefoneInput = document.getElementById('telefone');
    if (telefoneInput) {
        telefoneInput.addEventListener('input', function() {
            formatTelefone(this);
        });
    }
});