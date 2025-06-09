<?php
namespace App\Exports;

use App\Models\AttendanceSummary;
use Maatwebsite\Excel\Concerns\FromQuery;
use Maatwebsite\Excel\Concerns\Exportable;
use Maatwebsite\Excel\Concerns\WithHeadings;
use Maatwebsite\Excel\Concerns\WithMapping;
use Illuminate\Support\Facades\Log; // Tambahkan ini untuk logging

class AttendancesSummaryExport implements FromQuery, WithHeadings, WithMapping
{
    use Exportable;

    protected $filters;

    public function __construct($filters = [])
    {
        $this->filters = $filters;
    }

    public function query()
    {
        $query = AttendanceSummary::query()->with('user');

        if (!empty($this->filters['year'])) {
            $query->where('year', $this->filters['year']);
        }
        
        if (!empty($this->filters['month'])) {
            $query->where('month', $this->filters['month']);
        }

        Log::info('Query executed:', [$query->toSql(), $query->getBindings()]); // Log query yang dijalankan
        return $query;
    }

    public function headings(): array
    {
        return [
            'Nomor',
            'Nama',
            'Bulan',
            'Tahun',
            'Hadir',
            'Izin',
            'Sakit',
            'Terlambat',
            'Alpha'
        ];
    }

    public function map($attendanceSummary): array
    {
        return [
            $attendanceSummary->id,
            $attendanceSummary->user->name,
            $attendanceSummary->month,
            $attendanceSummary->year,
            $attendanceSummary->present_count,
            $attendanceSummary->leave_count,
            $attendanceSummary->sick_count,
            $attendanceSummary->late_count,
            $attendanceSummary->alpa_count,
        ];
    }
}