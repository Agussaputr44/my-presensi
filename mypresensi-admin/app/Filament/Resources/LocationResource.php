<?php

namespace App\Filament\Resources;

use App\Filament\Resources\LocationResource\Pages;
use App\Models\Location;
use Filament\Forms;
use Filament\Forms\Form;
use Filament\Forms\Components\TextInput;
use Filament\Resources\Resource;
use Filament\Tables;
use Filament\Tables\Columns\TextColumn;
use Filament\Tables\Table;

class LocationResource extends Resource
{
    protected static ?string $model = Location::class;

    protected static ?string $navigationIcon = 'heroicon-o-map';
    protected static ?string $navigationLabel = 'Kelola Lokasi';
    protected static ?string $navigationGroup = 'Kelola';

    public static function form(Forms\Form $form): Form
    {
        return $form
            ->schema([
                TextInput::make('name')
                    ->label('Nama Lokasi')
                    ->required()
                    ->maxLength(100),

                // Update the form to include 'alamat' and 'keterangan' instead of latitude, longitude, and radius
                TextInput::make('alamat')
                    ->label('Alamat')
                    ->required()
                    ->maxLength(255),

                TextInput::make('keterangan')
                    ->label('Keterangan')
                    ->required()
                    ->maxLength(500),
            ]);
    }

    public static function table(Tables\Table $table): Table
    {
        return $table
            ->columns([
                TextColumn::make('name')
                    ->label('Nama Lokasi')
                    ->searchable()
                    ->sortable(),

                // Update the table columns to show 'alamat' and 'keterangan' instead of latitude, longitude, and radius
                TextColumn::make('alamat')
                    ->label('Alamat')
                    ->sortable(),

                TextColumn::make('keterangan')
                    ->label('Keterangan')
                    ->sortable(),

                // TextColumn::make('created_at')
                //     ->label('Dibuat Pada')
                //     ->dateTime()
                //     ->sortable(),

                // TextColumn::make('updated_at')
                //     ->label('Diperbarui Pada')
                //     ->dateTime()
                //     ->sortable(),
            ])
            ->filters([/* Add filters if needed */])
            ->actions([
                Tables\Actions\EditAction::make(),
            ])
            ->bulkActions([
                Tables\Actions\DeleteBulkAction::make(),
            ]);
    }

    public static function getRelations(): array
    {
        return [
            //
        ];
    }

    public static function getPages(): array
    {
        return [
            'index' => Pages\ListLocations::route('/'),
            'create' => Pages\CreateLocation::route('/create'),
            'edit' => Pages\EditLocation::route('/{record}/edit'),
        ];
    }
}
