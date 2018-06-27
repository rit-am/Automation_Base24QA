cls
[GC]::Collect()
Write-Host ("Start Execution    @ " + (Get-Date(Get-Date).ToUniversalTime()-uformat "%s"))
$file = "C:\Users\"+$env:UserName+"\Downloads\data\txt\xpnet.log"   
GET-Process-Xpnet_Audit $file
Write-Host ("Complete Execution @ " + (Get-Date(Get-Date).ToUniversalTime()-uformat "%s"))





Function GET-Audit-MSGTI() {
    Param(
        [parameter(Mandatory=$false)][String]$IntegerVal,
        [parameter(Mandatory=$false)][String]$StringVal
        )
    $IntegerTempVariable=$IntegerRecordHeaderFoundAt+2
    if (Select-String -InputObject $StringArrayText[$IntegerTempVariable] -Pattern "B24 ISO8583 1993 message") {
            Write-Host(" ")
            
            #GET-ISO8583-DE "37" "RRN";GET-ISO8583-DE "11" "STAN";GET-ISO8583-DE "38" "Approval code";GET-ISO8583-DE "35" "Track 2";
            #GET-ISO8583-DE "39" "Code";GET-ISO8583-DE " 2" "PAN";GET-ISO8583-DE " 3" "Processing code";GET-ISO8583-DE " 4" "mount";
            Write-Host(" ISO Msg - Data retrival ")
            GET-ISO8583-extract


        }
    else {
        Write-Host(" Non ISO Msg - Skipping Data retrival ")
        }
    }

Function GET-ISO8583-DE() {
    Write-Host(" Getting ISE DE ")
    for ($loopfor_ISODE=$IntegerRecordHeaderFoundAt;$loopfor_ISODE-le $IntegerRecordFooterFoundAt; $loopfor_ISODE++) {
        if(Select-String -InputObject $StringArrayText[$loopfor_ISODE] -Pattern $IntegerDE) {
            if(Select-String -InputObject $StringArrayText[$loopfor_ISODE] -Pattern $StringDE) {
                    if($StringArrayText[$loopfor_ISODE].IndexOf($IntegerDE) -le $StringArrayText[$loopfor_ISODE].IndexOf($StringDE))
                        {
                        Write-Host("Possible Value for "+ $IntegerDE +" " + $StringDE+" @ Ln "+$loopfor_ISODE+" :# "+ 
                                                $StringArrayText[$loopfor_ISODE].Substring(
                                                    $StringArrayText[$loopfor_ISODE].IndexOf(":"),
                                                    $StringArrayText[$loopfor_ISODE].length - 
                                                    $StringArrayText[$loopfor_ISODE].IndexOf(":")) )
                        }
                }
            }
        
        }
    }

Function GET-Process-Xpnet_Audit() {
    Param([parameter(Mandatory=$true)][String]$StringAuditFileName)
    $StringArrayText=Get-Content -Path $StringAuditFileName;$StringArrayText.GetType() #|Format-Table -AutoSize
    $IntegerTextFileLength=$StringArrayText.Count
    #$IntegerTextFileLength=4
    $IntegerTextFileLengthStart=0;$RecordHeaderFound=0;$RecordFooterFound=0;$IntegerRecordCounter=0
    For ($loop=$IntegerTextFileLengthStart;$loop–le$IntegerTextFileLength;$loop++){
        
        #Write-Host($StringArrayText[$loop])
        #Write-Host(" ")

        #Start Search for XPNET HEADER using EXPLODE 
        if($RecordHeaderFound-eq 0){
            if($StringArrayText[$loop].Length -eq 79){
                $StringRecordHeaderCheck2=$StringArrayText[$loop].Substring(46,4);$StringRecordHeaderCheck1=$StringArrayText[$loop].Substring(0,1)
                    if(($StringRecordHeaderCheck1-eq"R")-and($StringRecordHeaderCheck2-eq"Len ")){$IntegerRecordHeaderFoundAt=$loop;$RecordHeaderFound=1;Write-Host(" ")}
                }
        }
        #Stop Search for XPNET HEADER using EXPLODE 
        
        #Start Search for XPNET FOOTER using EXPLODE 
        if(($RecordFooterFound-eq 0)-and($RecordHeaderFound-eq 1)){
            if($StringArrayText[$loop].Length -eq 42){
                $StringRecordFooterCheck1=$StringArrayText[$loop].Substring(20,22);$StringRecordFooterCheck2=$StringArrayText[$loop].Substring(0,1)
                if(($StringRecordFooterCheck1-eq"--- End of Message ---")-and($StringRecordFooterCheck2-eq"(")) 
                    {$IntegerRecordFooterFoundAt=$loop;$RecordFooterFound=1;$IntegerRecordCounter=$IntegerRecordCounter+1;}
            }
         }
         #Stop Search for XPNET FOOTER using EXPLODE 


         #START Check if we found HEader and footer
         if(($RecordFooterFound-eq 1)-and($RecordHeaderFound-eq 1)){
            
            #Write-Host("Header Found : #" + $IntegerRecordHeaderFoundAt + " |Data : " + $StringArrayText[$IntegerRecordHeaderFoundAt]);
            #Write-Host("Footer Found : #" + $IntegerRecordFooterFoundAt  + " |Data : " + $StringArrayText[$IntegerRecordFooterFoundAt]);

            GET-Audit-MSGTI;Write-Host(" ")

            #START ClearFlags
            $RecordHeaderFound=$RecordFooterFound=$StringMsgTypeFoundAt=$IntegerRRNFoundAt=$IntegerRecordHeaderFoundAt=$IntegerRecordFooterFoundAt=0;
            #END ClearFlags

            
            }
         #STOP Check if we found HEader and footer


        
        }#For-Loop
    }