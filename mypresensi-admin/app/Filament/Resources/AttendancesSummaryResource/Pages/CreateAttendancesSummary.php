<?php

namespace App\Filament\Resources\AttendancesSummaryResource\Pages;

use App\Filament\Resources\AttendancesSummaryResource;
use Filament\Pages\Actions;
use Filament\Resources\Pages\CreateRecord;

class CreateAttendancesSummary extends CreateRecord
{
    protected static string $resource = AttendancesSummaryResource::class;

    // Pastikan untuk memanggil calculateSummary setelah data dibuat
    public function afterSave()
    {
        parent::afterSave();

        $data = $this->getFormState();
        $this->record->calculateSummary($data['user_id'], $data['month'], $data['year']);
    }
}
