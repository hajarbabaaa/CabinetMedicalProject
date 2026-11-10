# Correction de RendezVousDAO.java 
$content = Get-Content 'src\com\cabinetmedical\dao\RendezVousDAO.java' 
$content = $content -replace 'import model\.RendezVous;', 'import com.cabinetmedical.model.RendezVous;' 
if (-not ($content -match 'import.*DatabaseConnection')) { 
    for ($i = 0; $i -lt $content.Length; $i++) { 
        if ($content[$i] -match 'import') { 
            $content = $content[0..($i-1)] + @('import com.cabinetmedical.config.DatabaseConnection;') + $content[$i..($content.Length-1)] 
            break 
        } 
    } 
} 
$content = $content -replace 'DatabaseConnection\.getInstance\(\)\.getConnection\(\)', 'DatabaseConnection.getConnection()' 
Set-Content 'src\com\cabinetmedical\dao\RendezVousDAO.java' $content 
